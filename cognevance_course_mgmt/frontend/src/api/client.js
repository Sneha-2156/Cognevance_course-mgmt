const API_BASE = import.meta.env.VITE_API_BASE || '/api'

// Wraps fetch: adds the JWT (if present) and throws with the server's error message
async function request(path, { method = 'GET', body, auth = true } = {}) {
  const headers = { 'Content-Type': 'application/json' }

  if (auth) {
    const token = localStorage.getItem('token')
    if (token) headers['Authorization'] = `Bearer ${token}`
  }

  const res = await fetch(`${API_BASE}${path}`, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  })

  const contentType = res.headers.get('content-type') || ''
  const data = contentType.includes('application/json') ? await res.json() : null

  if (!res.ok) {
    const message = data?.error || Object.values(data || {})[0] || 'Request failed'
    throw new Error(message)
  }
  return data
}

export const api = {
  register: (payload) => request('/auth/register', { method: 'POST', body: payload, auth: false }),
  login: (payload) => request('/auth/login', { method: 'POST', body: payload, auth: false }),

  getCourses: (params = {}) => {
    const qs = new URLSearchParams(params).toString()
    return request(`/courses${qs ? `?${qs}` : ''}`, { auth: false })
  },
  createCourse: (payload) => request('/courses', { method: 'POST', body: payload }),
  updateCourse: (id, payload) => request(`/courses/${id}`, { method: 'PUT', body: payload }),
  deleteCourse: (id) => request(`/courses/${id}`, { method: 'DELETE' }),

  enroll: (courseId) => request(`/enrollments/${courseId}`, { method: 'POST' }),
  myEnrollments: () => request('/enrollments/me'),
  updateProgress: (courseId, progress) =>
    request(`/enrollments/${courseId}/progress`, { method: 'PUT', body: { progress } }),
  enrollmentsForCourse: (courseId) => request(`/enrollments/course/${courseId}`),
}
