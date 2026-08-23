import { useEffect, useRef } from 'react';
import { Link } from 'react-router';
import CourseDetail from '../components/CourseDetail.jsx';
import CourseList from '../components/CourseList.jsx';
import DataControls from '../components/DataControls.jsx';
import ErrorMessage from '../components/ErrorMessage.jsx';
import FilterPanel from '../components/FilterPanel.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import OptimisticStatusControls from '../components/OptimisticStatusControls.jsx';
import PaginationControls from '../components/PaginationControls.jsx';
import SummaryCards from '../components/SummaryCards.jsx';
import { useCourseData } from '../context/CourseDataContext.jsx';

export default function CoursesPage() {
  const initialLoadRef = useRef(false);

  const {
    items,
    visibleCourses,
    selectedCourse,
    selectedCourseId,
    loading,
    error,
    pageInfo,
    filters,
    cacheMessage,
    updatingId,
    loadCoursesPage,
    refreshCourses,
    setSearchText,
    setLevelFilter,
    setStatusFilter,
    selectCourse,
    changeCourseStatus
  } = useCourseData();

  useEffect(() => {
    if (initialLoadRef.current) {
      return;
    }

    initialLoadRef.current = true;
    loadCoursesPage();
  }, [loadCoursesPage]);

  return (
    <>
      <SummaryCards courses={items} />

      <DataControls
        pageInfo={pageInfo}
        cacheMessage={cacheMessage}
        loading={loading}
        onRefresh={refreshCourses}
        onPageSizeChange={(size) => loadCoursesPage({ page: 0, size })}
        onSortChange={(sortBy, direction) => loadCoursesPage({ page: 0, sortBy, direction })}
      />

      <FilterPanel
        searchText={filters.searchText}
        statusFilter={filters.statusFilter}
        levelFilter={filters.levelFilter}
        onSearchChange={setSearchText}
        onStatusChange={setStatusFilter}
        onLevelChange={setLevelFilter}
      />

      {loading && <LoadingMessage message="Loading course page..." />}
      {error && <ErrorMessage message={error} />}

      <section className="workspace-grid">
        <CourseList
          courses={visibleCourses}
          selectedCourseId={selectedCourseId || selectedCourse?.id}
          onSelectCourse={(course) => selectCourse(course.id)}
        />
        <div className="course-list">
          <CourseDetail course={selectedCourse} />
          <OptimisticStatusControls
            course={selectedCourse}
            updatingId={updatingId}
            onStatusChange={changeCourseStatus}
          />
          {selectedCourse && (
            <Link className="button-link secondary" to={`/app/courses/${selectedCourse.id}/edit`}>
              Edit selected course
            </Link>
          )}
        </div>
      </section>

      <PaginationControls
        pageInfo={pageInfo}
        loading={loading}
        onPageChange={(page) => loadCoursesPage({ page })}
      />
    </>
  );
}