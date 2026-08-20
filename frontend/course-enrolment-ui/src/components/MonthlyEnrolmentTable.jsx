export default function MonthlyEnrolmentTable({ items = [] }) {
  // Helper to convert "YYYY-MM" (e.g., "2026-08") to "August 2026"
  const formatMonth = (dateStr) => {
    if (!dateStr) return 'Unspecified';
    const [year, month] = dateStr.split('-');
    if (!year || !month) return dateStr;

    const date = new Date(parseInt(year, 10), parseInt(month, 10) - 1);
    return date.toLocaleString('default', { month: 'long', year: 'numeric' });
  };

  // Calculate total overall enrolments across all months
  const totalEnrolments = items.reduce((sum, item) => sum + (item.count || 0), 0);

  return (
    <section className="card monthly-enrolments-card">
      <div className="section-heading">
        <h2>Monthly Enrolment Totals</h2>
        <p>Tracks registration trends and total student enrolments over time.</p>
      </div>

      <div className="table-container">
        <table className="overview-table">
          <thead>
            <tr>
              <th>Month</th>
              <th>Total Enrolments</th>
              <th>Enrolled Students</th>
            </tr>
          </thead>
          <tbody>
            {items.length === 0 ? (
              <tr>
                <td colSpan="3" className="empty-state">No monthly enrolment data available.</td>
              </tr>
            ) : (
              items.map((item, index) => (
                <tr key={index}>
                  <td>
                    <strong>{formatMonth(item.label)}</strong>
                  </td>
                  <td className="count-cell">
                    <strong>{item.count ?? 0} {item.count === 1 ? 'enrolment' : 'enrolments'}</strong>
                  </td>
                  <td>
                    {item.studentNames && item.studentNames.length > 0 ? (
                      <span>{item.studentNames.join(', ')}</span>
                    ) : (
                      <span className="text-muted" style={{ color: '#888' }}>No students recorded</span>
                    )}
                  </td>
                </tr>
              ))
            )}

            {/* Total Summary Row */}
            <tr className="total-courses-row">
              <td><strong>Total Cumulative Enrolments</strong></td>
              <td className="count-cell">
                <strong>{totalEnrolments} enrolments</strong>
              </td>
              <td>—</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  );
}