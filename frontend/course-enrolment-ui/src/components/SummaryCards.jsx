export default function SummaryCards({ courses = [] }) {
    const total = courses.length;
    const active = courses.filter((course) => course.status === 'ACTIVE').length;
    const inactive = courses.filter((course) => course.status === 'INACTIVE').length;

    return (
        <section className="summary-grid" aria-label="Course summary">
            <SummaryCard label="Total Courses" value={total} />
            <SummaryCard label="Active" value={active} />
            <SummaryCard label="Inactive" value={inactive} />
        </section>
    );
}

function SummaryCard({ label, value }) {
    return (
        <article className="summary-card">
            <p>{label}</p>
            <strong>{value}</strong>
        </article>
    );
}