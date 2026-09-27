import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext.jsx'

// Wrap a page with <ProtectedRoute> to require login, or
// <ProtectedRoute adminOnly> to require an ADMIN account.
export default function ProtectedRoute({ children, adminOnly = false }) {
  const { user, isAdmin } = useAuth()

  if (!user) return <Navigate to="/login" replace />
  if (adminOnly && !isAdmin) return <Navigate to="/dashboard" replace />

  return children
}
