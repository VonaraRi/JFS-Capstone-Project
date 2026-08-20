import { NavLink } from 'react-router';
import { useAuth } from '../context/AuthContext.jsx';

export default function StudentDashboardPage() {
  const { user } = useAuth();

  return (
    <div className="dashboard-container">
      {/* Welcome Header Card */}
      <section className="card welcome-card">
        <div className="welcome-info">
          <p className="eyebrow">Student Portal</p>
          <h2>Welcome, {user?.name || user?.email || 'Student'}!</h2>
          <p>
            Use your student dashboard to manage your course enrollments and view your profile information.
          </p>
        </div>
        <div className="action-row">
          <span className="status-badge status-active">STUDENT</span>
        </div>
      </section>

      {/* Quick Actions Card */}
      <div className="workspace-grid">
        <section className="card">
          <div className="section-heading">
            <h2>Quick Actions</h2>
            <p>What would you like to do today?</p>
          </div>
          <div className="quick-actions-row">
            <NavLink to="/app/student-profile" className="button-primary">
              View My Profile & Enrolled Courses
            </NavLink>
            <NavLink to="/app/register-course" className="button-secondary">
              Browse & Register Courses
            </NavLink>
          </div>
        </section>
      </div>
    </div>
  );
}