import { useEffect, useState } from 'react';
import ErrorMessage from '../components/ErrorMessage.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { fetchCourses, fetchMyEnrollments } from '../services/api.js';

export default function StudentProfilePage() {
  const { token, user } = useAuth();
  const [myEnrollments, setMyEnrollments] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    async function loadProfileData() {
      try {
        setLoading(true);
        setError('');

        const [allCourses, enrolledData] = await Promise.all([
          fetchCourses(token),
          fetchMyEnrollments(token)
        ]);

        const activeEnrollments = (enrolledData || []).filter(
          (item) => item.status === 'ENROLLED'
        );

        // Map full course details onto each enrolment record
        const courseMap = new Map((allCourses || []).map((c) => [c.id, c]));
        const enrichedEnrollments = activeEnrollments.map((enrolment) => ({
          ...enrolment,
          courseDetails: courseMap.get(enrolment.courseId) || enrolment.course || {}
        }));

        setMyEnrollments(enrichedEnrollments);
      } catch (err) {
        setError(err.message || 'Failed to load profile data.');
      } finally {
        setLoading(false);
      }
    }

    loadProfileData();
  }, [token]);

  if (loading) return <LoadingMessage message="Loading profile..." />;

  return (
    <div className="profile-container">
      {/* Student Personal Info Card */}
      <section className="card welcome-card">
        <div className="user-details">
          <p className="eyebrow">Student Account Information</p>
          <h2>{user?.name || 'Student Name'}</h2>
          <p><strong>Email:</strong> {user?.email}</p>
          <p><strong>Role:</strong> {user?.role}</p>
        </div>
        <div className="action-row">
          <span className="status-badge status-active">ACTIVE STUDENT</span>
        </div>
      </section>

      {error && <ErrorMessage message={error} />}

      {/* Enrolled Courses List */}
      <section className="card">
        <div className="section-heading">
          <h2>Registered Courses</h2>
          <p>All courses you are currently enrolled in</p>
        </div>

        <div className="course-list">
          {myEnrollments.length === 0 ? (
            <p className="empty-state">You have not registered for any courses yet.</p>
          ) : (
            myEnrollments.map((enrolment) => (
              <CourseRowItem
                key={enrolment.id}
                enrolment={enrolment}
                onViewDetails={(course) => setSelectedCourse(course)}
              />
            ))
          )}
        </div>
      </section>

      {/* Course Info Modal */}
      {selectedCourse && (
        <CourseDetailModal
          course={selectedCourse}
          onClose={() => setSelectedCourse(null)}
        />
      )}
    </div>
  );
}

// Sub-component for individual course row
function CourseRowItem({ enrolment, onViewDetails }) {
  const details = enrolment.courseDetails || {};
  const title = details.title || enrolment.courseTitle || enrolment.title || 'Course Title';
  const code = details.courseCode || details.code || enrolment.courseCode || enrolment.code || 'N/A';

  return (
    <div className="course-row">
      <div className="course-meta">
        <strong className="course-title">{title}</strong>
        <span className="course-code">Course Code: {code}</span>
      </div>
      <div className="action-row">
        <button
          type="button"
          className="badge-btn view-badge-btn"
          onClick={() => onViewDetails(details)}
        >
          View
        </button>
        <span className="status-badge status-active">Enrolled</span>
      </div>
    </div>
  );
}

// Sub-component for detailed course view modal
function CourseDetailModal({ course, onClose }) {
  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2 className="modal-title">{course.title || 'Course Details'}</h2>
        
        <div className="modal-body">
          <p><strong>Course Code:</strong> {course.courseCode || course.code || 'N/A'}</p>
          <p><strong>Category:</strong> {course.category || 'General'}</p>
          <p><strong>Level:</strong> {course.level || 'N/A'}</p>
          <div className="modal-description-box">
            <strong>Description:</strong>
            <p>{course.description || 'No description available.'}</p>
          </div>
        </div>

        <div className="modal-actions">
          <button type="button" className="button-primary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
}