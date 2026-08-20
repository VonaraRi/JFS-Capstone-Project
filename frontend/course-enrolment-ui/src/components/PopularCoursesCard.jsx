export default function PopularCoursesCard({ title = "Most Popular Courses", items = [] }) {
  // Find maximum count to scale visual pillar heights dynamically
  const maxCount = Math.max(...items.map((item) => item.count ?? 0), 1);

  return (
    <section className="card popular-courses-card">
      <div className="section-heading">
        <h2>{title}</h2>
        <p>Highlights the top-ranked courses sorted by highest enrolment count.</p>
      </div>

      {!items || items.length === 0 ? (
        <p className="empty-state">No enrolment records available.</p>
      ) : (
        <div className="popular-grid">
          {items.map((item, index) => {
            const studentCount = item.count ?? 0;
            const rank = index + 1;
            // Calculate height percentage for visual bar (minimum 35% height)
            const pillarHeight = Math.max(Math.round((studentCount / maxCount) * 100), 35);

            return (
              <div key={index} className={`popular-card-item rank-${rank}`}>
                <div className="course-top-title">{item.label || "Unknown Course"}</div>

                <div className="pill-badge">
                  <span>Enrolled: {studentCount}</span>
                </div>

                <div className="visual-pillar-container">
                  <div
                    className="visual-pillar"
                    style={{ height: `${pillarHeight}%` }}
                  >
                    <span className="rank-tag">#{rank}</span>
                  </div>
                </div>

                <div className="course-bottom-info">
                  <strong>{item.label || "Unknown Course"}</strong>
                  <span className="rating-text">★ {studentCount} {studentCount === 1 ? 'student' : 'students'}</span>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </section>
  );
}