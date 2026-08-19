import { createContext, useCallback, useContext, useMemo, useReducer } from 'react';
import { fetchPagedCourses, updateCourse } from '../services/api.js';
import { filterCourses } from '../utils/courses.js';
import { useAuth } from './AuthContext.jsx';

const CourseDataContext = createContext(null);

const initialState = {
  items: [],
  selectedCourseId: '',
  loading: false,
  error: '',
  cacheMessage: 'No cached page loaded yet.',
  updatingId: '',
  cache: {},
  pageInfo: {
    page: 0,
    size: 5,
    sortBy: 'courseCode',
    direction: 'asc',
    totalPages: 0,
    totalElements: 0
  },
  filters: {
    searchText: '',
    statusFilter: 'ALL',
    levelFilter: 'ALL'
  }
};

function makeCacheKey(params) {
  return `${params.page}|${params.size}|${params.sortBy}|${params.direction}`;
}

function getItemId(item) {
  return item?._id || item?.id;
}

function replaceCourse(items, updatedCourse) {
  const updatedId = getItemId(updatedCourse);
  return items.map((course) => (getItemId(course) === updatedId ? updatedCourse : course));
}

function replaceCourseInCache(cache, updatedCourse) {
  const nextCache = {};

  Object.entries(cache).forEach(([key, pageData]) => {
    nextCache[key] = {
      ...pageData,
      content: replaceCourse(pageData.content ?? [], updatedCourse)
    };
  });

  return nextCache;
}

function toPageInfo(data, fallback) {
  return {
    page: data.number ?? fallback.page,
    size: data.size ?? fallback.size,
    sortBy: fallback.sortBy,
    direction: fallback.direction,
    totalPages: data.totalPages ?? 0,
    totalElements: data.totalElements ?? 0
  };
}

function courseReducer(state, action) {
  switch (action.type) {
    case 'LOAD_START':
      return {
        ...state,
        loading: true,
        error: '',
        cacheMessage: action.fromCache ? 'Reading from cache...' : 'Fetching from backend...'
      };

    case 'LOAD_SUCCESS': {
      const items = action.data.content ?? [];
      const selectedStillVisible = items.some((course) => getItemId(course) === state.selectedCourseId);
      const selectedCourseId = selectedStillVisible ? state.selectedCourseId : getItemId(items[0]) ?? '';
      const nextCache = action.fromCache
        ? state.cache
        : { ...state.cache, [action.cacheKey]: action.data };

      return {
        ...state,
        items,
        selectedCourseId,
        loading: false,
        error: '',
        pageInfo: toPageInfo(action.data, action.params),
        cache: nextCache,
        cacheMessage: action.fromCache ? 'Loaded from cache.' : 'Fetched from backend and cached.'
      };
    }

    case 'LOAD_ERROR':
      return {
        ...state,
        loading: false,
        error: action.message,
        cacheMessage: 'Could not load data.'
      };

    case 'SET_SEARCH_TEXT':
      return {
        ...state,
        filters: { ...state.filters, searchText: action.value }
      };

    case 'SET_STATUS_FILTER':
      return {
        ...state,
        filters: { ...state.filters, statusFilter: action.value }
      };

    case 'SET_LEVEL_FILTER':
      return {
        ...state,
        filters: { ...state.filters, levelFilter: action.value }
      };

    case 'SELECT_COURSE':
      return {
        ...state,
        selectedCourseId: action.courseId
      };

    case 'OPTIMISTIC_UPDATE':
      return {
        ...state,
        updatingId: getItemId(action.course),
        items: replaceCourse(state.items, action.course),
        cache: replaceCourseInCache(state.cache, action.course)
      };

    case 'UPDATE_SUCCESS':
      return {
        ...state,
        updatingId: '',
        items: replaceCourse(state.items, action.course),
        cache: replaceCourseInCache(state.cache, action.course),
        cacheMessage: 'Optimistic update confirmed by backend.'
      };

    case 'ROLLBACK_UPDATE':
      return {
        ...state,
        updatingId: '',
        items: replaceCourse(state.items, action.course),
        cache: replaceCourseInCache(state.cache, action.course),
        error: action.message,
        cacheMessage: 'Optimistic update rolled back.'
      };

    default:
      return state;
  }
}

function toUpdatePayload(course) {
  return {
    courseCode: course.courseCode || course.code,
    title: course.title,
    category: course.category,
    level: course.level,
    status: course.status,
    capacity: course.capacity,
    description: course.description ?? null
  };
}

export function CourseDataProvider({ children }) {
  const { token } = useAuth();
  const [state, dispatch] = useReducer(courseReducer, initialState);

  const loadCoursesPage = useCallback(async (overrides = {}) => {
    const params = {
      page: overrides.page ?? state.pageInfo.page,
      size: overrides.size ?? state.pageInfo.size,
      sortBy: overrides.sortBy ?? state.pageInfo.sortBy,
      direction: overrides.direction ?? state.pageInfo.direction
    };

    const cacheKey = makeCacheKey(params);
    const cachedPage = state.cache[cacheKey];

    if (cachedPage && !overrides.force) {
      dispatch({
        type: 'LOAD_SUCCESS',
        data: cachedPage,
        params,
        cacheKey,
        fromCache: true
      });
      return;
    }

    dispatch({ type: 'LOAD_START', fromCache: false });

    try {
      const data = await fetchPagedCourses(token, params);
      dispatch({
        type: 'LOAD_SUCCESS',
        data,
        params,
        cacheKey,
        fromCache: false
      });
    } catch (error) {
      dispatch({
        type: 'LOAD_ERROR',
        message: error.message || 'Could not load paged courses.'
      });
    }
  }, [state.cache, state.pageInfo, token]);

  const refreshCourses = useCallback(() => {
    return loadCoursesPage({ force: true });
  }, [loadCoursesPage]);

  const setSearchText = useCallback((value) => {
    dispatch({ type: 'SET_SEARCH_TEXT', value });
  }, []);

  const setStatusFilter = useCallback((value) => {
    dispatch({ type: 'SET_STATUS_FILTER', value });
  }, []);

  const setLevelFilter = useCallback((value) => {
    dispatch({ type: 'SET_LEVEL_FILTER', value });
  }, []);

  const selectCourse = useCallback((courseId) => {
    dispatch({ type: 'SELECT_COURSE', courseId });
  }, []);

  const changeCourseStatus = useCallback(async (courseId, nextStatus) => {
    const currentCourse = state.items.find((course) => getItemId(course) === courseId);

    if (!currentCourse || currentCourse.status === nextStatus) {
      return;
    }

    const optimisticCourse = { ...currentCourse, status: nextStatus };
    dispatch({ type: 'OPTIMISTIC_UPDATE', course: optimisticCourse });

    try {
      const savedCourse = await updateCourse(courseId, token, toUpdatePayload(optimisticCourse));
      dispatch({ type: 'UPDATE_SUCCESS', course: savedCourse });
    } catch (error) {
      dispatch({
        type: 'ROLLBACK_UPDATE',
        course: currentCourse,
        message: error.message || 'Could not update course status. Reverted local change.'
      });
    }
  }, [state.items, token]);

  const changeCourseLevel = useCallback(async (courseId, nextLevel) => {
    const currentCourse = state.items.find((course) => getItemId(course) === courseId);

    if (!currentCourse || currentCourse.level === nextLevel) {
      return;
    }

    const optimisticCourse = { ...currentCourse, level: nextLevel };
    dispatch({ type: 'OPTIMISTIC_UPDATE', course: optimisticCourse });

    try {
      const savedCourse = await updateCourse(courseId, token, toUpdatePayload(optimisticCourse));
      dispatch({ type: 'UPDATE_SUCCESS', course: savedCourse });
    } catch (error) {
      dispatch({
        type: 'ROLLBACK_UPDATE',
        course: currentCourse,
        message: error.message || 'Could not update course level. Reverted local change.'
      });
    }
  }, [state.items, token]);

  const visibleCourses = useMemo(
    () =>
      filterCourses(
        state.items,
        state.filters.searchText,
        state.filters.statusFilter,
        state.filters.levelFilter
      ),
    [state.items, state.filters]
  );

  const selectedCourse = useMemo(() => {
    return visibleCourses.find((course) => getItemId(course) === state.selectedCourseId) ?? visibleCourses[0] ?? null;
  }, [state.selectedCourseId, visibleCourses]);

  const value = useMemo(
    () => ({
      ...state,
      visibleCourses,
      selectedCourse,
      loadCoursesPage,
      refreshCourses,
      setSearchText,
      setStatusFilter,
      setLevelFilter,
      selectCourse,
      changeCourseStatus,
      changeCourseLevel
    }),
    [
      state,
      visibleCourses,
      selectedCourse,
      loadCoursesPage,
      refreshCourses,
      setSearchText,
      setStatusFilter,
      setLevelFilter,
      selectCourse,
      changeCourseStatus,
      changeCourseLevel
    ]
  );

  return <CourseDataContext.Provider value={value}>{children}</CourseDataContext.Provider>;
}

export function useCourseData() {
  const value = useContext(CourseDataContext);

  if (!value) {
    throw new Error('useCourseData must be used inside CourseDataProvider');
  }

  return value;
}