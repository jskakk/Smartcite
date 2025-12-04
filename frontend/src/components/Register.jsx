import React, {useState} from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api'
import './Auth.css'

export default function Register(){
  const [email, setEmail] = useState('')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const navigate = useNavigate()

  const handleRegister = async (e) => {
    e.preventDefault()
    
    if (password !== confirmPassword) {
      alert('Passwords do not match!')
      return
    }
    
    try{
      const payload = { email, name: username, password }
      const res = await api.post('/users', payload)
      if(res.data && res.data.success){
        alert('Registration successful!')
        navigate('/login')
      }
    }catch(err){
      alert('Register failed: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1 className="auth-title">Register</h1>
        <form onSubmit={handleRegister} className="auth-form">
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
            <label>Username</label>
            <input 
              type="text" 
              value={username} 
              onChange={e=>setUsername(e.target.value)} 
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
          <div className="auth-field">
            <label>Confirm Password</label>
            <input 
              type="password" 
              value={confirmPassword} 
              onChange={e=>setConfirmPassword(e.target.value)} 
              required
            />
          </div>
          <button className="auth-btn primary" type="submit">Register</button>
          <Link to="/login">
            <button type="button" className="auth-btn secondary">Login</button>
          </Link>
        </form>
      </div>
    </div>
  )
}
