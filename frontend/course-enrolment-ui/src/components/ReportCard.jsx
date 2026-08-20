export default function ReportCard({ title, items = [] }) {
  return (
    <section className="card report-card">
      <div className="section-heading">
        <h2>{title}</h2>
        <p>Aggregated from course enrolment backend data.</p>
      </div>

      <div className="report-list">
        {!items || items.length === 0 ? (
          <p className="empty-state">No report data recorded yet.</p>
        ) : (
          items.map((item, index) => (
            <div className="report-row" key={item.label || index}>
              <span>{item.label || 'Unassigned'}</span>
              <strong>{item.count ?? 0}</strong>
            </div>
          ))
        )}
      </div>
    </section>
  );
}