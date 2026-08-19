import { useEffect, useMemo, useState } from 'react';
import CourseDetail from '../components/CourseDetail.jsx';
import CourseList from '../components/CourseList.jsx';
import ErrorMessage from '../components/ErrorMessage.jsx';
import FilterPanel from '../components/FilterPanel.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import SummaryCards from '../components/SummaryCards.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { fetchCourses } from '../services/api.js';
import { filterCourses } from '../utils/courses.js';

export default function CoursesPage() {
  const { token } = useAuth();
  const [courses, setCourses] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState(null);
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [levelFilter, setLevelFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const filteredCourses = useMemo(
    () => filterCourses(courses, searchText, statusFilter, levelFilter),
    [courses, searchText, statusFilter, levelFilter]
  );

  useEffect(() => {
    let ignore = false;

    async function loadCourses() {
      try {
        setLoading(true);
        setError('');
        const data = await fetchCourses(token);

        if (!ignore) {
          setCourses(data);
          setSelectedCourse(data[0] ?? null);
        }
      } catch (err) {
        if (!ignore) {
          setError(err.message || 'Could not load protected course data.');
          console.error(err);
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    }

    loadCourses();

    return () => {
      ignore = true;
    };
  }, [token]);

  useEffect(() => {
    if (filteredCourses.length === 0) {
      setSelectedCourse(null);
      return;
    }

    const selectedStillVisible = filteredCourses.some((course) => course.id === selectedCourse?.id);

    if (!selectedStillVisible) {
      setSelectedCourse(filteredCourses[0]);
    }
  }, [filteredCourses, selectedCourse]);

  if (loading) {
    return <LoadingMessage message="Loading protected courses..." />;
  }

  if (error) {
    return <ErrorMessage message={error} />;
  }

  return (
    <>
      <SummaryCards courses={courses} />
      <FilterPanel
        searchText={searchText}
        statusFilter={statusFilter}
        levelFilter={levelFilter}
        onSearchChange={setSearchText}
        onStatusChange={setStatusFilter}
        onLevelChange={setLevelFilter}
      />
      <section className="workspace-grid">
        <CourseList
          courses={filteredCourses}
          selectedCourseId={selectedCourse?.id}
          onSelectCourse={setSelectedCourse}
        />
        <CourseDetail course={selectedCourse} />
      </section>
    </>
  );
}