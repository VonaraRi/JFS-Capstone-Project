import { apiRequest, buildQueryString } from './httpClient.js';

export async function fetchApiInfo() {
  return apiRequest('/api/v1/info');
}

export async function fetchApiDocs() {
  return apiRequest('/api/docs');
}

export async function loginRequest(email, password) {
  return apiRequest('/api/auth/login', {
    method: 'POST',
    body: { email, password }
  });
}

export async function fetchCourses(token) {
  return apiRequest('/api/v1/courses', { token });
}

export async function fetchPagedCourses(token, params) {
  const queryString = buildQueryString({
    page: params.page,
    size: params.size,
    sortBy: params.sortBy,
    direction: params.direction
  });

  return apiRequest(`/api/v1/courses/paged${queryString}`, { token });
}

export async function fetchCourseById(id, token) {
  return apiRequest(`/api/v1/courses/${id}`, { token });
}

export async function createCourse(token, payload) {
  return apiRequest('/api/v1/courses', {
    method: 'POST',
    token,
    body: payload
  });
}

export async function updateCourse(id, token, payload) {
  return apiRequest(`/api/v1/courses/${id}`, {
    method: 'PUT',
    token,
    body: payload
  });
}

export async function fetchReport(path, token) {
  return apiRequest(path, { token });
}

export async function fetchCourseReports(token) {
  const [
    byStatus,
    byLevel,
    byCategory,
    byCapacity,
    enrolmentsPerCourse,
    popularCourses,
    overview,
    monthlyEnrolments
  ] = await Promise.all([
    fetchReport('/api/v1/reports/courses-by-status', token),
    fetchReport('/api/v1/reports/courses-by-level', token),
    fetchReport('/api/v1/reports/courses-by-category', token),
    fetchReport('/api/v1/reports/courses-by-capacity', token),
    
    fetchReport('/api/v1/reports/enrolments-per-course', token),
    fetchReport('/api/v1/reports/popular-courses', token),
    fetchReport('/api/v1/reports/enrolments-overview', token),
    fetchReport('/api/v1/reports/monthly-enrolments', token)
  ]);

  return {
    byStatus,
    byLevel,
    byCategory,
    byCapacity,
    enrolmentsPerCourse,
    popularCourses,
    overview,
    monthlyEnrolments
  };
}

export async function enrollCourse(courseId, token) {
  return apiRequest('/api/v1/enrolments', {
    method: 'POST',
    token,
    body: { courseId }
  });
}

// Calls your backend drop endpoint using the enrolment ID
export async function dropEnrolment(enrolmentId, token) {
  return apiRequest(`/api/v1/enrolments/${enrolmentId}/drop`, {
    method: 'PUT',
    token
  });
}

export async function fetchMyEnrollments(token) {
  return apiRequest('/api/v1/enrolments/my-courses', { token });
}