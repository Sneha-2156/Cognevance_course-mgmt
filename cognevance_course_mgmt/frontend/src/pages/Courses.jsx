import { useState, useEffect, useCallback } from 'react'
import { api } from '../api/client.js'
import { useAuth } from '../context/AuthContext.jsx'

const CATEGORIES = ['All', 'Frontend', 'Backend', 'Full Stack', 'Data', 'DevOps']

export default function Courses() {
  const { user, isAdmin } = useAuth()
  const [courses, setCourses] = useState([])
  const [search, setSearch] = useState('')
  const [category, setCategory] = useState('All')
  const [loading, setLoading] = useState(true)
  const [message, setMessage] = useState('')

  const loadCourses = useCallback(async () => {
    setLoading(true)
    try {
      const params = {}
      if (search) params.search = search
      if (category !== 'All') params.category = category
      const data = await api.getCourses(params)
      setCourses(data)
    } catch (err) {
      setMessage(err.message)
    } finally {
      setLoading(false)
    }
  }, [search, category])

  useEffect(() => {
    const timeout = setTimeout(loadCourses, 300) // debounce search typing
    return () => clearTimeout(timeout)
  }, [loadCourses])

  const handleEnroll = async (courseId) => {
    try {
      await api.enroll(courseId)
      setMessage('Enrolled! Check your dashboard.')
    } catch (err) {
      setMessage(err.message)
    }
  }

  return (
    <section className="section">
      <h2 className="section__title">Browse Courses</h2>

      <div className="course-filters">
        <input
          type="text"
          placeholder="Search courses..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          {CATEGORIES.map((c) => <option key={c} value={c}>{c}</option>)}
        </select>
      </div>

      {message && <p className="form-status">{message}</p>}
      {loading ? (
        <p>Loading courses...</p>
      ) : courses.length === 0 ? (
        <p>No courses match your search.</p>
      ) : (
        <div className="course-grid">
          {courses.map((c) => (
            <div className="course-card" key={c.id}>
              <span className="tag">{c.category}</span>
              <h3>{c.title}</h3>
              <p>{c.description}</p>
              <p className="course-card__instructor">Instructor: {c.instructor}</p>
              {user && !isAdmin && (
                <button className="btn btn--primary btn--sm" onClick={() => handleEnroll(c.id)}>
                  Enroll
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </section>
  )
}
