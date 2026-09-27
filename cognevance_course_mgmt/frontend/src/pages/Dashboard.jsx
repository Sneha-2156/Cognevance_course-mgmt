import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api/client.js'

export default function Dashboard() {
  const [enrollments, setEnrollments] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const load = async () => {
    setLoading(true)
    try {
      const data = await api.myEnrollments()
      setEnrollments(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const handleProgressChange = async (courseId, newProgress) => {
    // Optimistic update so the slider feels instant
    setEnrollments((prev) =>
      prev.map((e) => (e.courseId === courseId ? { ...e, progress: newProgress } : e))
    )
    try {
      await api.updateProgress(courseId, newProgress)
    } catch (err) {
      setError(err.message)
      load() // revert to server truth on failure
    }
  }

  return (
    <section className="section">
      <h2 className="section__title">My Dashboard</h2>
      {error && <p className="form-status form-status--error">{error}</p>}

      {loading ? (
        <p>Loading your courses...</p>
      ) : enrollments.length === 0 ? (
        <p>You haven't enrolled in any courses yet. <Link to="/courses">Browse courses</Link>.</p>
      ) : (
        <div className="enrollment-list">
          {enrollments.map((e) => (
            <div className="enrollment-card" key={e.enrollmentId}>
              <div className="enrollment-card__header">
                <span className="tag">{e.category}</span>
                <h3>{e.courseTitle}</h3>
              </div>
              <div className="progress-bar">
                <div className="progress-bar__fill" style={{ width: `${e.progress}%` }} />
              </div>
              <div className="progress-controls">
                <input
                  type="range" min="0" max="100" value={e.progress}
                  onChange={(ev) => handleProgressChange(e.courseId, Number(ev.target.value))}
                />
                <span>{e.progress}%</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </section>
  )
}
