import React from 'react'
import { Navigate } from 'react-router-dom'

export default function PublicRoute({ children, isAuthenticated }) {
  // If user is already logged in, redirect to dashboard
  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }
  return children
}
