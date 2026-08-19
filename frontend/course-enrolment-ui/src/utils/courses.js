export function filterCourses(courses, searchText, statusFilter, levelFilter) {
    const normalizedSearch = searchText.trim().toLowerCase();

    return courses.filter((course) => {
        const matchesSearch =
            normalizedSearch.length === 0 ||
            course.courseCode.toLowerCase().includes(normalizedSearch) ||
            course.title.toLowerCase().includes(normalizedSearch) ||
            course.category.toLowerCase().includes(normalizedSearch) ||
            course.capacity.toLowerCase().includes(normalizedSearch);

        const matchesStatus = statusFilter === 'ALL' || course.status === statusFilter;

        const matchesLevel = levelFilter === 'ALL' || course.level === levelFilter;

        return matchesSearch && matchesStatus && matchesLevel;
    });
}

export function countByStatus(courses, status) {
    return courses.filter((course) => course.status === status).length;
}

export function countByLevel(courses, level) {
    return courses.filter((course) => course.level === level).length;
}