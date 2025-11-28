import React, {useState} from 'react'
import { useNavigate, Link } from 'react-router-dom'
import api from '../api'

export default function Login(){
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleLogin = async (e) => {
    e.preventDefault()
    try{
      // Simple example: check user exists by email then redirect to dashboard
      const res = await api.get(`/users/email/${encodeURIComponent(email)}`)
      if(res.data && res.data.success){
        // in real app, validate password; here we assume success
        navigate('/dashboard')
      }
    }catch(err){
      alert('Login failed: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="card">
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div className="form-field">
          <label>Email</label>
          <input value={email} onChange={e=>setEmail(e.target.value)} />
        </div>
        <div className="form-field">
          <label>Password</label>
          <input type="password" value={password} onChange={e=>setPassword(e.target.value)} />
        </div>
        <div className="flex-row">
          <button className="btn" type="submit">Login</button>
          <Link to="/register"><button type="button" className="btn">Register</button></Link>
        </div>
      </form>
    </div>
  )
}
