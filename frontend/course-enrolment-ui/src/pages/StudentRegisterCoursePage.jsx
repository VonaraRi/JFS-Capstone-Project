import { useEffect, useState } from 'react';
import ErrorMessage from '../components/ErrorMessage.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { dropEnrolment, enrollCourse, fetchCourses, fetchMyEnrollments } from '../services/api.js';

export default function StudentRegisterCoursePage() {
  const { token } = useAuth();
  const [courses, setCourses] = useState([]);
  const [myEnrollments, setMyEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    loadCourseCatalog();
  }, [token]);

  async function loadCourseCatalog() {
    try {
      setLoading(true);
      setError('');
      const [allCoursesData, enrolledData] = await Promise.all([
        fetchCourses(token),
        fetchMyEnrollments(token)
      ]);

      // Filter out inactive courses so students never see or register for them
      const activeCourses = (allCoursesData || []).filter(
        (course) => course.status && course.status.toUpperCase() !== 'INACTIVE'
      );
      setCourses(activeCourses);

      // Filter only active "ENROLLED" records so dropped courses don't count as enrolled
      const activeEnrolments = (enrolledData || []).filter(
        (item) => item.status === 'ENROLLED'
      );
      setMyEnrollments(activeEnrolments);
    } catch (err) {
      setError(err.message || 'Failed to load course catalog.');
    } finally {
      setLoading(false);
    }
  }

  async function handleEnroll(courseId) {
    try {
      setSuccessMessage('');
      setError('');
      await enrollCourse(courseId, token);
      setSuccessMessage('Successfully registered for the course!');
      await loadCourseCatalog();
    } catch (err) {
      setError(err.message || 'Registration failed.');
    }
  }

  async function handleDrop(enrolmentId) {
    try {
      setSuccessMessage('');
      setError('');
      await dropEnrolment(enrolmentId, token);
      setSuccessMessage('Successfully dropped the course.');
      await loadCourseCatalog();
    } catch (err) {
      setError(err.message || 'Failed to drop course.');
    }
  }

  if (loading) return <LoadingMessage message="Loading available courses..." />;

  // Map courseId -> enrolmentId for quick lookup
  const activeEnrolmentMap = new Map(
    myEnrollments.map((item) => [item.courseId, item.id])
  );

  return (
    <div className="page-grid">
      <section className="card">
        <div className="section-heading">
          <h2>Register for Courses</h2>
          <p>Select from available courses to add them to your schedule</p>
        </div>

        {error && <ErrorMessage message={error} />}
        {successMessage && <p className="message success-message">{successMessage}</p>}

        <div className="course-list">
          {courses.length === 0 ? (
            <p className="empty-state">No courses available for registration.</p>
          ) : (
            courses.map((course) => {
              const enrolmentId = activeEnrolmentMap.get(course.id);
              const isEnrolled = activeEnrolmentMap.has(course.id);

              // Standardized check for course full status
              const isFull =
                course.capacity &&
                course.capacity.toString().trim().toUpperCase() === 'FULL';

              return (
                <div key={course.id} className="course-row">
                  <div>
                    <strong>{course.title}</strong>
                    <span>Code: {course.courseCode || course.code} | Level: {course.level}</span>
                  </div>

                  {isEnrolled ? (
                    <button
                      type="button"
                      className="button-secondary unenroll-btn"
                      onClick={() => handleDrop(enrolmentId)}
                    >
                      Dropped
                    </button>
                  ) : isFull ? (
                    <button type="button" className="button-secondary" disabled>
                      FULL
                    </button>
                  ) : (
                    <button
                      type="button"
                      className="button-link"
                      onClick={() => handleEnroll(course.id)}
                    >
                      Enroll Now
                    </button>
                  )}
                </div>
              );
            })
          )}
        </div>
      </section>
    </div>
  );
}