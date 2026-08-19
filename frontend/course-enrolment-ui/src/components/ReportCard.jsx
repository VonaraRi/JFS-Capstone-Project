export default function ReportCard({ title, items }) {
  return (
    <section className="card report-card">
      <div className="section-heading">
        <h2>{title}</h2>
        <p>Aggregated from course enrolment backend data.</p>
      </div>

      <div className="report-list">
        {items?.map((item) => (
          <div className="report-row" key={item.label}>
            <span>{item.label}</span>
            <strong>{item.count}</strong>
          </div>
        ))}
      </div>
    </section>
  );
}