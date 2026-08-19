async function parseJsonResponse(response) {
  const contentType = response.headers.get('content-type') ?? '';
  const body = contentType.includes('application/json') ? await response.json() : null;

  if (!response.ok) {
    const message = body?.message || `Request failed with status ${response.status}`;
    throw new Error(message);
  }

  return body;
}

export async function fetchApiInfo() {
  const response = await fetch('/api/v1/info');
  return parseJsonResponse(response);
}

export async function fetchApiDocs() {
  const response = await fetch('/api/docs');
  return parseJsonResponse(response);
}

export async function loginRequest(email, password) {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ email, password })
  });

  return parseJsonResponse(response);
}

export async function fetchCourses(token) {
  const response = await fetch('/api/v1/courses', {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  return parseJsonResponse(response);
}

export async function fetchReport(path, token) {
  const response = await fetch(path, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });

  return parseJsonResponse(response);
}

export async function fetchCourseReports(token) {
  // Added 'byCapacity' to the array destructuring assignment
  const [byStatus, byLevel, byCategory, byCapacity] = await Promise.all([
    fetchReport('/api/v1/reports/courses-by-status', token),
    fetchReport('/api/v1/reports/courses-by-level', token),
    fetchReport('/api/v1/reports/courses-by-category', token),
    fetchReport('/api/v1/reports/courses-by-capacity', token)
  ]);

  return { byStatus, byLevel, byCategory, byCapacity };
}