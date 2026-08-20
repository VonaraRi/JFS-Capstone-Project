export default function EnrolmentsOverviewTable({ overview }) {
  if (!overview) {
    return (
      <section className="card overview-card">
        <div className="section-heading">
          <h2>Enrolments Overview</h2>
        </div>
        <p className="empty-state">No overview data available.</p>
      </section>
    );
  }

  const sections = [
    { title: "Level", data: overview.level || [] },
    { title: "Status", data: overview.status || [] },
    { title: "Category", data: overview.category || [] },
    { title: "Capacity", data: overview.capacity || [] },
  ];

  return (
    <section className="card overview-card">
      <div className="section-heading">
        <h2>Enrolments Overview</h2>
        <p>Summary of unique student enrolments grouped by course attributes.</p>
      </div>

      <div className="table-container">
        <table className="overview-table">
          <thead>
            <tr>
              <th>Type of Attribute</th>
              <th>Attribute Value</th>
              <th>Total Students</th>
            </tr>
          </thead>
          <tbody>
            {sections.map((section, sectionIdx) => {
              const rows = section.data.length > 0 
                ? section.data 
                : [{ label: "N/A", count: 0 }];

              return rows.map((item, rowIdx) => (
                <tr key={`${sectionIdx}-${rowIdx}`}>
                  {rowIdx === 0 && (
                    <td rowSpan={rows.length} className="attribute-type-cell">
                      <strong>{section.title}</strong>
                    </td>
                  )}
                  <td>{item.label || "Unspecified"}</td>
                  <td className="count-cell">
                    <strong>{item.count ?? 0}</strong>
                  </td>
                </tr>
              ));
            })}
            
            <tr className="total-courses-row">
              <td><strong>Total Courses</strong></td>
              <td colSpan="2" className="total-courses-value">
                <strong>{overview.totalCourses ?? 0} courses</strong>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  );
}