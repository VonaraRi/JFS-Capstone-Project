import EmptyState from "./EmptyState";
import LevelBadge from "./LevelBadge";
import StatusBadge from "./StatusBadge";

export default function CourseDetail({ course }) {
    if (!course) {
        return <EmptyState message="Select a course to view more information."/>;
    }

    return (
        <section className="card detail-card">
            <div className="section-heading row-heading">
                <div>
                    <h2>{course.title}</h2>
                    <p>{course.courseCode}</p>
                </div>
                <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
                    <StatusBadge status={course.status} />
                    <LevelBadge level={course.level} />
                </div>
            </div>

            <dl className="detail-list">
                <div>
                    <dt>Description</dt>
                    <dd>{course.description}</dd>
                </div>
                <div>
                    <dt>Category</dt>
                    <dd>{course.category}</dd>
                </div>
                <div>
                    <dt>Level</dt>
                    <dd>{course.level}</dd>
                </div>
                <div>
                    <dt>Capacity</dt>
                    <dd>{course.capacity}</dd>
                </div>
                <div>
                    <dt>Created At</dt>
                    <dd>
                        {course.createdAt
                            ? new Date(course.createdAt).toLocaleDateString("en-US", {
                                year: "numeric",
                                month: "short",
                                day: "numeric"
                              })
                            : "N/A"}
                    </dd>
                </div>
            </dl>
        </section>
    );
}