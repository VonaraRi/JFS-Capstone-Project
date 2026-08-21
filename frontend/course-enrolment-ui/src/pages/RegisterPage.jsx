import { useState } from "react";
import { Link } from "react-router";
import { apiRequest } from "../services/httpClient";

export default function RegisterPage() {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "STUDENT"
  });
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess(false);

    try {
      await apiRequest("/api/auth/register", {
        method: "POST",
        body: formData
      });
      setSuccess(true);
    } catch (err) {
      setError(err.message || "Registration failed");
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2 className="register-title">Student Registration</h2>
        <p className="register-subtitle">Create an account to enroll in courses</p>

        {/* Success Alert Banner */}
        {success && (
          <div className="alert alert-success">
            <span>Account created successfully! You can now log in.</span>
            <Link to="/login" className="btn-login-now">
              Login Now
            </Link>
          </div>
        )}

        {/* Error Alert */}
        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        {/* Registration Form */}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Full Name</label>
            <input
              type="text"
              name="name"
              placeholder="Enter your full name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Email Address</label>
            <input
              type="email"
              name="email"
              placeholder="student@example.com"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              placeholder="Minimum 8 characters"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Role</label>
            <input
              type="text"
              value="Student"
              disabled
              className="disabled-input"
            />
          </div>

          <button type="submit" className="submit-btn">
            Register
          </button>
        </form>

        <p className="footer-text">
          Already have an account? <Link to="/login" className="footer-link">Login</Link>
        </p>
      </div>
    </div>
  );
}