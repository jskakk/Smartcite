import React, {useState} from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function Register(){
  const [email, setEmail] = useState('')
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const handleRegister = async (e) => {
    e.preventDefault()
    try{
      const payload = { email, username, password }
      const res = await api.post('/users', payload)
      if(res.data && res.data.success){
        navigate('/login')
      }
    }catch(err){
      alert('Register failed: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="card">
      <h2>Register</h2>
      <form onSubmit={handleRegister}>
        <div className="form-field"><label>Email</label><input value={email} onChange={e=>setEmail(e.target.value)} /></div>
        <div className="form-field"><label>Username</label><input value={username} onChange={e=>setUsername(e.target.value)} /></div>
        <div className="form-field"><label>Password</label><input type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <div className="form-field"><label>Confirm Password</label><input type="password" /></div>
        <button className="btn" type="submit">Register</button>
      </form>
    </div>
  )
}
