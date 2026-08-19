export function filterCourses(courses = [], searchText = '', statusFilter = 'ALL', levelFilter = 'ALL') {
    const normalizedSearch = (searchText || '').trim().toLowerCase();
    const normalizedStatus = (statusFilter || 'ALL').toUpperCase();
    const normalizedLevel = (levelFilter || 'ALL').toUpperCase();

    return courses.filter((course) => {
        // Safe string conversions for optional backend fields
        const code = (course.courseCode || '').toLowerCase();
        const title = (course.title || '').toLowerCase();
        const category = (course.category || '').toLowerCase();
        const capacity = String(course.capacity ?? '').toLowerCase();

        const matchesSearch =
            normalizedSearch.length === 0 ||
            code.includes(normalizedSearch) ||
            title.includes(normalizedSearch) ||
            category.includes(normalizedSearch) ||
            capacity.includes(normalizedSearch);

        const courseStatus = (course.status || '').toUpperCase();
        const matchesStatus =
            normalizedStatus === 'ALL' ||
            normalizedStatus === '' ||
            courseStatus === normalizedStatus;

        const courseLevel = (course.level || '').toUpperCase();
        const matchesLevel =
            normalizedLevel === 'ALL' ||
            normalizedLevel === '' ||
            courseLevel === normalizedLevel;

        return matchesSearch && matchesStatus && matchesLevel;
    });
}

export function countByStatus(courses = [], status = '') {
    const targetStatus = status.toUpperCase();
    return courses.filter((course) => (course.status || '').toUpperCase() === targetStatus).length;
}

export function countByLevel(courses = [], level = '') {
    const targetLevel = level.toUpperCase();
    return courses.filter((course) => (course.level || '').toUpperCase() === targetLevel).length;
}