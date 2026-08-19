import { useRef, useState, useEffect } from 'react';
import FormStepIndicator from './FormStepIndicator.jsx';
import InlineFieldError from './InlineFieldError.jsx';
import ErrorMessage from './ErrorMessage.jsx';

const STATUS_OPTIONS = ['ACTIVE', 'INACTIVE'];
const LEVEL_OPTIONS = ['Beginner', 'Intermediate', 'Advanced'];

export const emptyCourseForm = {
  code: '',
  title: '',
  category: '',
  level: '',
  status: 'ACTIVE',
  capacity: '',
  description: ''
};

export default function CourseFormWizard({
  mode = 'create',
  initialValues = emptyCourseForm,
  onSubmit,
  saving = false,
  serverError = '',
  successMessage = ''
}) {
  const [step, setStep] = useState(1);
  const [formValues, setFormValues] = useState({ ...emptyCourseForm, ...initialValues });
  const [fieldErrors, setFieldErrors] = useState({});
  const reviewCheckboxRef = useRef(null);

  const isEditMode = mode === 'edit';

  useEffect(() => {
    setFormValues({ ...emptyCourseForm, ...initialValues });
  }, [initialValues]);

  function updateField(fieldName, value) {
    setFormValues((current) => ({
      ...current,
      [fieldName]: value
    }));

    setFieldErrors((current) => ({
      ...current,
      [fieldName]: ''
    }));
  }

  function validateStep(stepToValidate) {
    const errors = {};

    if (stepToValidate === 1) {
      if (!formValues.code.trim()) {
        errors.code = 'Course code is required.';
      } else if (!/^[A-Z0-9-]+$/.test(formValues.code.trim())) {
        errors.code = 'Use uppercase letters, numbers and hyphens only.';
      }

      if (!formValues.title.trim()) {
        errors.title = 'Course title is required.';
      } else if (formValues.title.trim().length < 3) {
        errors.title = 'Course title must be at least 3 characters.';
      }

      if (!formValues.level.trim()) {
        errors.level = 'Level is required.';
      } else if (!LEVEL_OPTIONS.includes(formValues.level)) {
        errors.level = 'Choose a valid level.';
      }
    }

    if (stepToValidate === 2) {
      if (!formValues.category.trim()) {
        errors.category = 'Category is required.';
      }

      if (!STATUS_OPTIONS.includes(formValues.status)) {
        errors.status = 'Choose a valid status.';
      }

      // Validates string capacity presence
      if (!formValues.capacity.trim()) {
        errors.capacity = 'Capacity is required.';
      }
    }

    if (stepToValidate === 3 && !reviewCheckboxRef.current?.checked) {
      errors.review = 'Please confirm that you reviewed the course details.';
    }

    setFieldErrors(errors);
    return Object.keys(errors).length === 0;
  }

  function goToNextStep() {
    if (validateStep(step)) {
      setStep((current) => Math.min(current + 1, 3));
    }
  }

  function goToPreviousStep() {
    setFieldErrors({});
    setStep((current) => Math.max(current - 1, 1));
  }

  async function handleSubmit(event) {
    event.preventDefault();

    if (!validateStep(3)) {
      return;
    }

    const payload = {
      code: formValues.code.trim(),
      title: formValues.title.trim(),
      category: formValues.category.trim(),
      level: formValues.level.trim(),
      status: formValues.status,
      capacity: formValues.capacity.trim(),
      description: formValues.description.trim() || null
    };

    await onSubmit(payload);
  }

  return (
    <form className="card course-form" onSubmit={handleSubmit} noValidate>
      <div className="section-heading">
        <h2>{isEditMode ? 'Update Course' : 'Create Course'}</h2>
        <p>Fill out course details across three steps before final review.</p>
      </div>

      <FormStepIndicator currentStep={step} />

      {serverError && <ErrorMessage message={serverError} />}
      {successMessage && <p className="message success-message">{successMessage}</p>}

      {step === 1 && (
        <section className="form-grid" aria-label="Course identity">
          <label htmlFor="code">
            Course Code
            <input
              id="code"
              value={formValues.code}
              onChange={(event) => updateField('code', event.target.value.toUpperCase())}
              aria-describedby="code-error"
            />
            <InlineFieldError id="code-error" message={fieldErrors.code} />
          </label>

          <label htmlFor="title">
            Course Title
            <input
              id="title"
              value={formValues.title}
              onChange={(event) => updateField('title', event.target.value)}
              aria-describedby="title-error"
            />
            <InlineFieldError id="title-error" message={fieldErrors.title} />
          </label>

          <label htmlFor="level" className="form-grid-full">
            Level
            <select
              id="level"
              value={formValues.level}
              onChange={(event) => updateField('level', event.target.value)}
              aria-describedby="level-error"
            >
              <option value="">Select Level</option>
              {LEVEL_OPTIONS.map((lvl) => (
                <option key={lvl} value={lvl}>
                  {lvl}
                </option>
              ))}
            </select>
            <InlineFieldError id="level-error" message={fieldErrors.level} />
          </label>
        </section>
      )}

      {step === 2 && (
        <section className="form-grid" aria-label="Category and status">
          <label htmlFor="category">
            Category
            <input
              id="category"
              value={formValues.category}
              onChange={(event) => updateField('category', event.target.value)}
              aria-describedby="category-error"
            />
            <InlineFieldError id="category-error" message={fieldErrors.category} />
          </label>

          <label htmlFor="status">
            Status
            <select
              id="status"
              value={formValues.status}
              onChange={(event) => updateField('status', event.target.value)}
              aria-describedby="status-error"
            >
              {STATUS_OPTIONS.map((status) => (
                <option key={status} value={status}>{status}</option>
              ))}
            </select>
            <InlineFieldError id="status-error" message={fieldErrors.status} />
          </label>

          <label htmlFor="capacity">
            Capacity
            <input
              id="capacity"
              type="text"
              value={formValues.capacity}
              onChange={(event) => updateField('capacity', event.target.value)}
              placeholder="e.g. 2 seats left or Full"
              aria-describedby="capacity-error"
            />
            <InlineFieldError id="capacity-error" message={fieldErrors.capacity} />
          </label>

          <label htmlFor="description" className="form-grid-full">
            Description (Optional)
            <textarea
              id="description"
              value={formValues.description}
              onChange={(event) => updateField('description', event.target.value)}
              rows={3}
              placeholder="Provide course overview..."
              aria-describedby="description-error"
            />
            <InlineFieldError id="description-error" message={fieldErrors.description} />
          </label>
        </section>
      )}

      {step === 3 && (
        <section aria-label="Review course details">
          <div className="review-grid">
            {Object.entries(formValues).map(([key, value]) => (
              <div key={key} className="info-item">
                <span>{formatLabel(key)}</span>
                <strong>{value !== '' && value !== null ? String(value) : 'Not specified'}</strong>
              </div>
            ))}
          </div>

          <label className="review-check">
            <input ref={reviewCheckboxRef} type="checkbox" />
            I have reviewed the course details and they are ready to submit.
          </label>
          <InlineFieldError message={fieldErrors.review} />
        </section>
      )}

      <div className="form-actions">
        {step > 1 && (
          <button type="button" className="button-link secondary" onClick={goToPreviousStep}>
            Back
          </button>
        )}

        {step < 3 && (
          <button type="button" className="button-link" onClick={goToNextStep}>
            Continue
          </button>
        )}

        {step === 3 && (
          <button type="submit" className="button-link" disabled={saving}>
            {saving ? 'Saving...' : isEditMode ? 'Update Course' : 'Create Course'}
          </button>
        )}
      </div>
    </form>
  );
}

function formatLabel(key) {
  return key
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, (letter) => letter.toUpperCase());
}