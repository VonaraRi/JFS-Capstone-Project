export default function FilterPanel ({ searchText, statusFilter, levelFilter, onSearchChange, onStatusChange, onLevelChange}) {
    return(
        <section className="filter-panel" aria-label="Course filters">
            <label>
                Search courses
                <input
                    type="search"
                    placeholder="Search by Course code, title, category, or capacity"
                    value={searchText}
                    onChange={(event) => onSearchChange(event.target.value)}
                />
            </label>

            <label>
                Status
                <select value={statusFilter} onChange={(event) => onStatusChange(event.target.value)}>
                    <option value="ALL">All</option>
                    <option value="ACTIVE">Active</option>
                    <option value="INACTIVE">Inactive</option>
                </select>
            </label>

            <label>
                Level
                <select value={levelFilter} onChange={(event) => onLevelChange(event.target.value)}>
                    <option value="ALL">All</option>
                    <option value="BEGINNER">Beginner</option>
                    <option value="INTERMEDIATE">Intermediate</option>
                    <option value="ADVANCED">Advanced</option>
                </select>
            </label>
        </section>
    );
}