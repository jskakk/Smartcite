import React, { useState, useEffect } from 'react'
import { Routes, Route, Link, useLocation } from 'react-router-dom'
import Landing from './components/Landing'
import Introduction from './components/Introduction'
import Login from './components/Login'
import Register from './components/Register'
import Dashboard from './components/Dashboard'
import Citation from './components/Citation'
import Result from './components/Result'
import CitationDetail from './components/CitationDetail'
import Grammar from './components/Grammar'
import Settings from './components/Settings'
import ProtectedRoute from './components/ProtectedRoute'
import PublicRoute from './components/PublicRoute'

export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false)
  const location = useLocation()

  useEffect(() => {
    // Check if user is logged in
    const user = localStorage.getItem('user')
    setIsAuthenticated(!!user)
  }, [])

  const handleLogout = () => {
    localStorage.removeItem('user')
    setIsAuthenticated(false)
    window.location.href = '/login'
  }

  const isAuthPage = location.pathname === '/login' || location.pathname === '/register'
  const isLandingPage = location.pathname === '/' && !isAuthenticated
  const isIntroPage = location.pathname === '/introduction'

  return (
    <div className="app">
      <nav className="topbar">
        <Link to="/" className="brand">SmartCite</Link>
        <div className="navlinks">
          {!isAuthPage && !isLandingPage && !isIntroPage && (
            isAuthenticated ? (
              <>
                <Link to="/dashboard">Home</Link>
                <Link to="/citation">Citation</Link>
                <Link to="/grammar">Grammar</Link>
                <Link to="/settings">Settings</Link>
                <button onClick={handleLogout} className="logout-btn">Logout</button>
              </>
            ) : (
              <>
                <Link to="/login">Login</Link>
                <Link to="/register">Register</Link>
              </>
            )
          )}
        </div>
      </nav>
      <main className="main">
        <Routes>
          <Route path="/" element={isAuthenticated ? <Dashboard/> : <Landing/>} />
          <Route path="/introduction" element={<Introduction/>} />
          <Route path="/login" element={<PublicRoute isAuthenticated={isAuthenticated}><Login setIsAuthenticated={setIsAuthenticated} /></PublicRoute>} />
          <Route path="/register" element={<PublicRoute isAuthenticated={isAuthenticated}><Register/></PublicRoute>} />
          <Route path="/dashboard" element={<ProtectedRoute isAuthenticated={isAuthenticated}><Dashboard/></ProtectedRoute>} />
          <Route path="/citation" element={<ProtectedRoute isAuthenticated={isAuthenticated}><Citation/></ProtectedRoute>} />
          <Route path="/citation/result" element={<ProtectedRoute isAuthenticated={isAuthenticated}><Result/></ProtectedRoute>} />
          <Route path="/citation/detail" element={<ProtectedRoute isAuthenticated={isAuthenticated}><CitationDetail/></ProtectedRoute>} />
          <Route path="/grammar" element={<ProtectedRoute isAuthenticated={isAuthenticated}><Grammar/></ProtectedRoute>} />
          <Route path="/settings" element={<ProtectedRoute isAuthenticated={isAuthenticated}><Settings/></ProtectedRoute>} />
        </Routes>
      </main>
    </div>
  )
}
