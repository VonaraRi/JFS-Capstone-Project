import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router';
import CourseFormWizard, { emptyCourseForm } from '../components/CourseFormWizard.jsx';
import ErrorMessage from '../components/ErrorMessage.jsx';
import LoadingMessage from '../components/LoadingMessage.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { createCourse, fetchCourseById, updateCourse } from '../services/api.js';

export default function CourseFormPage() {
  const { courseId } = useParams();
  const navigate = useNavigate();
  const { token, user } = useAuth();
  const [initialValues, setInitialValues] = useState(emptyCourseForm);
  const [loading, setLoading] = useState(Boolean(courseId));
  const [loadError, setLoadError] = useState('');
  const [saving, setSaving] = useState(false);
  const [serverError, setServerError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const isEditMode = Boolean(courseId);
  const isAdmin = user?.role === 'ADMIN';

  useEffect(() => {
    let ignore = false;

    async function loadCourseForEdit() {
      if (!courseId) {
        return;
      }

      try {
        setLoading(true);
        setLoadError('');
        const course = await fetchCourseById(courseId, token);

        if (!ignore) {
          setInitialValues({
            code: course.courseCode ?? course.code ?? '',
            title: course.title ?? '',
            category: course.category ?? '',
            level: course.level ?? '',
            status: course.status ?? 'ACTIVE',
            capacity: course.capacity ?? '',
            description: course.description ?? ''
          });
        }
      } catch (err) {
        if (!ignore) {
          setLoadError(err.message || 'Could not load course for editing.');
          console.error(err);
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    }

    loadCourseForEdit();

    return () => {
      ignore = true;
    };
  }, [courseId, token]);

  async function handleSubmit(payload) {
    try {
      setSaving(true);
      setServerError('');
      setSuccessMessage('');

      // Keep capacity as a clean String
      const formattedPayload = {
        courseCode: payload.code || payload.courseCode,
        title: payload.title,
        category: payload.category,
        level: payload.level,
        status: payload.status,
        capacity: payload.capacity ? String(payload.capacity).trim() : '',
        description: payload.description || null
      };

      if (isEditMode) {
        await updateCourse(courseId, token, formattedPayload);
        setSuccessMessage('Course updated successfully.');
      } else {
        await createCourse(token, formattedPayload);
        setSuccessMessage('Course created successfully.');
      }
    } catch (err) {
      setServerError(err.message || 'Could not save course.');
      console.error(err);
    } finally {
      setSaving(false);
    }
  }

  if (!isAdmin) {
    return (
      <section className="card">
        <div className="section-heading">
          <p className="eyebrow">Admin only</p>
          <h2>Course form is restricted</h2>
          <p>Only ADMIN users can create or update courses in this demo.</p>
        </div>
        <Link className="button-link" to="/app/courses">Back to Courses</Link>
      </section>
    );
  }

  if (loading) {
    return <LoadingMessage message="Loading course form..." />;
  }

  if (loadError) {
    return <ErrorMessage message={loadError} />;
  }

  return (
    <>
      <section className="card welcome-card">
        <div>
          <p className="eyebrow">Forms & validation</p>
          <h2>{isEditMode ? 'Edit existing course' : 'Create a new course'}</h2>
          <p>Fill out course details across three steps before final submission.</p>
        </div>
        <div className="action-row">
          <Link className="button-link secondary" to="/app/courses">Back to Courses</Link>
          {successMessage && (
            <button type="button" className="button-link" onClick={() => navigate('/app/courses')}>
              View Courses
            </button>
          )}
        </div>
      </section>

      <CourseFormWizard
        key={courseId || 'create'}
        mode={isEditMode ? 'edit' : 'create'}
        initialValues={initialValues}
        onSubmit={handleSubmit}
        saving={saving}
        serverError={serverError}
        successMessage={successMessage}
      />
    </>
  );
}