import EmptyState from "./EmptyState";
import StatusBadge from "./StatusBadge";

export default function CourseList({ courses, selectedCourseId, onSelectCourse }) {
    if (courses.length === 0) {
        return <EmptyState message="No courses match the current filter." />;
    }

    return (
        <section className="card list-card">
            <div className="section-heading">
                <h2>Course List</h2>
                <p>Select a course to view details.</p>
            </div>

            <div className="course-list">
                {courses.map((course, index) => (
                    <button
                        key={course.id || course.courseCode || index}
                        className={course.id === selectedCourseId ? 'course-row selected' : 'course-row'}
                        onClick={() => onSelectCourse(course)}
                        type="button"
                    >
                        <div>
                            <strong>{course.courseCode}</strong>
                            <span>{course.title}</span>
                        </div>
                        <StatusBadge status={course.status} />
                    </button>
                ))}
            </div>
        </section>
    );
}