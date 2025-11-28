import React from 'react'
import { Routes, Route, Link } from 'react-router-dom'
import Landing from './components/Landing'
import Login from './components/Login'
import Register from './components/Register'
import Dashboard from './components/Dashboard'
import Citation from './components/Citation'
import Result from './components/Result'
import Grammar from './components/Grammar'
import Settings from './components/Settings'

export default function App() {
  return (
    <div className="app">
      <nav className="topbar">
        <div className="brand">SmartCite</div>
        <div className="navlinks">
          <Link to="/">Home</Link>
          <Link to="/citation">Citation</Link>
          <Link to="/grammar">Grammar</Link>
          <Link to="/settings">Settings</Link>
        </div>
      </nav>
      <main className="main">
        <Routes>
          <Route path="/" element={<Landing/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register/>} />
          <Route path="/dashboard" element={<Dashboard/>} />
          <Route path="/citation" element={<Citation/>} />
          <Route path="/citation/result" element={<Result/>} />
          <Route path="/grammar" element={<Grammar/>} />
          <Route path="/settings" element={<Settings/>} />
        </Routes>
      </main>
    </div>
  )
}
