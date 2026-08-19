const STATUSES = ['ACTIVE', 'INACTIVE'];

export default function OptimisticStatusControls({ course, updatingId, onStatusChange }) {
  if (!course) {
    return null;
  }

  // Fallback to checking either _id or id for MongoDB compatibility
  const courseId = course._id || course.id;
  const isUpdating = updatingId === courseId;

  return (
    <section className="card">
      <div className="section-heading">
        <p className="eyebrow">Optimistic update</p>
        <h2>Quick status update</h2>
        <p>
          The UI updates immediately, then confirms with the backend. If the backend fails, it rolls back.
        </p>
      </div>
      <div className="action-row">
        {STATUSES.map((status) => (
          <button
            key={status}
            type="button"
            className={status === course.status ? 'button-link' : 'button-link secondary'}
            disabled={isUpdating || status === course.status}
            onClick={() => onStatusChange(courseId, status)}
          >
            {status}
          </button>
        ))}
      </div>
      {isUpdating && <p className="message loading-message">Saving status change...</p>}
    </section>
  );
}