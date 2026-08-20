import { useState } from 'react';

export default function EnrolmentsPerCourseCard({ title, items = [] }) {
  const [expandedRows, setExpandedRows] = useState({});

  const toggleRow = (index) => {
    setExpandedRows((prev) => ({
      ...prev,
      [index]: !prev[index],
    }));
  };

  return (
    <section className="card report-card">
      <div className="section-heading">
        <h2>{title}</h2>
        <p>Aggregated from course enrolment backend data.</p>
      </div>

      <div className="table-container">
        <table className="report-table">
          <thead>
            <tr>
              <th>Courses</th>
              <th>Total Students</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {!items || items.length === 0 ? (
              <tr>
                <td colSpan="3" className="empty-state">No enrolment records found.</td>
              </tr>
            ) : (
              items.map((item, index) => {
                const studentCount = item.count ?? 0;
                const unitLabel = studentCount === 1 ? 'student' : 'students';
                const isExpanded = !!expandedRows[index];

                return (
                  <tr key={index} style={{ borderBottom: isExpanded ? 'none' : '1px solid #f0f0f0' }}>
                    <td>
                      <strong>{item.label || 'Unknown Course'}</strong>
                      
                      {/* Expanded Student List */}
                      {isExpanded && (
                        <div className="student-list-container">
                          <p className="student-list-title">Enrolled Students:</p>
                          {item.studentNames && item.studentNames.length > 0 ? (
                            <ul className="student-list">
                              {item.studentNames.map((name, i) => (
                                <li key={i}>{name}</li>
                              ))}
                            </ul>
                          ) : (
                            <p className="no-students">No student details available.</p>
                          )}
                        </div>
                      )}
                    </td>
                    <td className="count-cell">
                      <strong>{studentCount} {unitLabel}</strong>
                    </td>
                    <td className="action-cell">
                      <button 
                        type="button" 
                        className="btn-view-students"
                        onClick={() => toggleRow(index)}
                      >
                        {isExpanded ? 'Hide' : 'View'} Students
                      </button>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </section>
  );
}