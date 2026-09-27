import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

export default function Navbar() {
  const { user, logout, isAdmin } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <header className="navbar">
      <div className="navbar__inner">
        <Link to="/" className="navbar__brand">CourseHub</Link>

        <nav className="navbar__links">
          <Link to="/courses">Courses</Link>
          {user && !isAdmin && <Link to="/dashboard">My Dashboard</Link>}
          {user && isAdmin && <Link to="/admin">Admin</Link>}

          {!user ? (
            <>
              <Link to="/login">Login</Link>
              <Link to="/register" className="btn btn--primary btn--sm">Sign Up</Link>
            </>
          ) : (
            <>
              <span className="navbar__user">Hi, {user.name.split(' ')[0]}</span>
              <button className="btn btn--ghost btn--sm" onClick={handleLogout}>Logout</button>
            </>
          )}
        </nav>
      </div>
    </header>
  )
}
