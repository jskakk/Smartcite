import React, {useState} from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api'
import './Auth.css'

export default function Login({ setIsAuthenticated }){
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleLogin = async (e) => {
    e.preventDefault()
    try{
      // Send email and password for authentication
      const res = await api.post('/users/login', {
        email: email,
        password: password
      })
      if(res.data && res.data.success){
        // Store user data in localStorage
        localStorage.setItem('user', JSON.stringify(res.data.data))
        setIsAuthenticated(true)
        navigate('/dashboard')
      }
    }catch(err){
      alert('Login failed: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1 className="auth-title">Login</h1>
        <form onSubmit={handleLogin} className="auth-form">
          <div className="auth-field">
            <label>Email</label>
            <input 
              type="email" 
              value={email} 
              onChange={e=>setEmail(e.target.value)} 
              required
            />
          </div>
          <div className="auth-field">
            <label>Password</label>
            <input 
              type="password" 
              value={password} 
              onChange={e=>setPassword(e.target.value)} 
              required
            />
          </div>
          <button className="auth-btn primary" type="submit">Login</button>
          <Link to="/register">
            <button type="button" className="auth-btn secondary">Register</button>
          </Link>
        </form>
      </div>
    </div>
  )
}
