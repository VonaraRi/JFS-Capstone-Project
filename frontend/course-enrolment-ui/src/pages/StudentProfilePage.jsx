import { useEffect, useState } from 'react';
import ErrorMessage from '../components/ErrorMessage.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { fetchMyEnrollments } from '../services/api.js';

export default function StudentProfilePage() {
  const { token, user } = useAuth();
  const [myEnrollments, setMyEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    async function loadProfileData() {
      try {
        setLoading(true);
        setError('');
        const enrolledData = await fetchMyEnrollments(token);

        // Filter for active enrolments
        const activeEnrollments = (enrolledData || []).filter(
          (item) => item.status === 'ENROLLED'
        );

        setMyEnrollments(activeEnrollments);
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
              <CourseRowItem key={enrolment.id} enrolment={enrolment} />
            ))
          )}
        </div>
      </section>
    </div>
  );
}

// Sub-component to isolate row rendering logic
function CourseRowItem({ enrolment }) {
  const title = enrolment.courseTitle || enrolment.title || enrolment.course?.title || 'Course Title';
  const code = enrolment.courseCode || enrolment.code || enrolment.course?.courseCode || 'N/A';

  return (
    <div className="course-row">
      <div className="course-meta">
        <strong className="course-title">{title}</strong>
        <span className="course-code">Course Code: {code}</span>
      </div>
      <span className="status-badge status-active">Enrolled</span>
    </div>
  );
}