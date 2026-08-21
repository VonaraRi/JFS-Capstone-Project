import { useState } from 'react';
import { Link, Navigate, useLocation, useNavigate } from 'react-router';
import ErrorMessage from '../components/ErrorMessage.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import { useAuth } from '../context/AuthContext.jsx';

export default function LoginPage() {
  const { user, isAuthenticated, login } = useAuth();
  const [email, setEmail] = useState('admin@example.com');
  const [password, setPassword] = useState('Admin@12345');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const location = useLocation();

  // Redirect authenticated users based on their role
  if (isAuthenticated) {
    const targetPath = user?.role === 'STUDENT' ? '/app/student-dashboard' : '/app/dashboard';
    return <Navigate to={targetPath} replace />;
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError('');

    try {
      const loggedInUser = await login(email, password);
      
      // Determine destination: respect redirected route if present, otherwise check role
      const defaultPath = loggedInUser?.role === 'STUDENT' ? '/app/student-dashboard' : '/app/dashboard';
      const destination = location.state?.from?.pathname || defaultPath;

      navigate(destination, { replace: true });
    } catch (err) {
      setError(err.message || 'Login failed. Check the backend and credentials.');
    } finally {
      setLoading(false);
    }
  }

  // Preset filler for quick testing
  function handleFillCredentials(presetEmail, presetPassword) {
    setEmail(presetEmail);
    setPassword(presetPassword);
  }

  return (
    <main className="login-page">
      <section className="login-card">
        <p className="eyebrow">Capstone Project</p>
        <h1>Login to Course Enrolment System</h1>
        <p>
          Authenticate with your credentials to access your dashboard.
        </p>

        <form onSubmit={handleSubmit} className="login-form">
          <label>
            Email
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
            />
          </label>

          <label>
            Password
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
            />
          </label>

          {error && <ErrorMessage message={error} />}
          {loading && <LoadingMessage message="Logging in..." />}

          <button type="submit" disabled={loading}>
            {loading ? 'Please wait...' : 'Login'}
          </button>
        </form>

        <div className="login-help">
          <strong>Seeded Test Credentials</strong>
          <div className="preset-buttons">
            <button
              type="button"
              className="button-secondary preset-btn"
              onClick={() => handleFillCredentials('admin@example.com', 'Admin@12345')}
            >
              Fill Admin
            </button>
            <button
              type="button"
              className="button-secondary preset-btn"
              onClick={() => handleFillCredentials('student@example.com', 'Student@12345')}
            >
              Fill Student
            </button>
          </div>
        </div>

        {/* Register link container */}
        <div className="login-register-prompt" style={{ marginTop: '20px', textAlign: 'center' }}>
          <span>Don't have an account? </span>
          <Link to="/register" style={{ color: '#800020', fontWeight: 'bold', textDecoration: 'none' }}>
            Register account
          </Link>
        </div>
      </section>
    </main>
  );
}