import { useState, useEffect } from 'react'
import { api } from '../api/client.js'

const emptyForm = { title: '', description: '', category: 'Backend', instructor: '' }

export default function Admin() {
  const [courses, setCourses] = useState([])
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [message, setMessage] = useState('')
  const [loading, setLoading] = useState(true)

  const load = async () => {
    setLoading(true)
    try {
      setCourses(await api.getCourses())
    } catch (err) {
      setMessage(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const resetForm = () => {
    setForm(emptyForm)
    setEditingId(null)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setMessage('')
    try {
      if (editingId) {
        await api.updateCourse(editingId, form)
        setMessage('Course updated.')
      } else {
        await api.createCourse(form)
        setMessage('Course created.')
      }
      resetForm()
      load()
    } catch (err) {
      setMessage(err.message)
    }
  }

  const handleEdit = (course) => {
    setEditingId(course.id)
    setForm({
      title: course.title,
      description: course.description || '',
      category: course.category,
      instructor: course.instructor,
    })
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this course? This cannot be undone.')) return
    try {
      await api.deleteCourse(id)
      load()
    } catch (err) {
      setMessage(err.message)
    }
  }

  return (
    <section className="section">
      <h2 className="section__title">Admin — Manage Courses</h2>

      <form className="admin-form" onSubmit={handleSubmit}>
        <h3>{editingId ? 'Edit Course' : 'New Course'}</h3>
        <div className="form-row">
          <label>Title</label>
          <input required value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} />
        </div>
        <div className="form-row">
          <label>Description</label>
          <textarea rows="3" value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })} />
        </div>
        <div className="form-row">
          <label>Category</label>
          <select value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })}>
            {['Frontend', 'Backend', 'Full Stack', 'Data', 'DevOps'].map((c) => (
              <option key={c} value={c}>{c}</option>
            ))}
          </select>
        </div>
        <div className="form-row">
          <label>Instructor</label>
          <input required value={form.instructor} onChange={(e) => setForm({ ...form, instructor: e.target.value })} />
        </div>
        <div className="admin-form__actions">
          <button className="btn btn--primary" type="submit">{editingId ? 'Save Changes' : 'Create Course'}</button>
          {editingId && <button type="button" className="btn btn--ghost" onClick={resetForm}>Cancel</button>}
        </div>
        {message && <p className="form-status">{message}</p>}
      </form>

      <h3 style={{ marginTop: 40 }}>Existing Courses</h3>
      {loading ? <p>Loading...</p> : (
        <table className="admin-table">
          <thead>
            <tr><th>Title</th><th>Category</th><th>Instructor</th><th></th></tr>
          </thead>
          <tbody>
            {courses.map((c) => (
              <tr key={c.id}>
                <td>{c.title}</td>
                <td>{c.category}</td>
                <td>{c.instructor}</td>
                <td className="admin-table__actions">
                  <button className="btn btn--ghost btn--sm" onClick={() => handleEdit(c)}>Edit</button>
                  <button className="btn btn--ghost btn--sm" onClick={() => handleDelete(c.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}
