import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'
import './Settings.css'

export default function Settings(){
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [saved, setSaved] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    // Load current user data
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser)
        setName(user.name || '')
        setEmail(user.email || '')
      } catch (e) {
        console.error('Failed to load user data', e)
      }
    }
  }, [])

  const handleSave = async (e) => {
    e.preventDefault()
    
    try {
      // Get current user data
      const storedUser = localStorage.getItem('user')
      if (!storedUser) {
        alert('User not found. Please log in again.')
        navigate('/login')
        return
      }

      const user = JSON.parse(storedUser)
      
      // Update user data on backend
      const response = await api.put(`/users/${user.viewId}`, {
        name: name,
        email: email,
        password: user.password // Keep existing password
      })

      if (response.data && response.data.success) {
        // Update localStorage with response from backend
        localStorage.setItem('user', JSON.stringify(response.data.data))
        
        setSaved(true)
        setTimeout(() => setSaved(false), 3000)
        
        // Force refresh to update all components
        window.dispatchEvent(new Event('storage'))
      }
    } catch (err) {
      console.error('Failed to update profile:', err)
      alert('Failed to update profile: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="settings-container">
      <div className="settings-card">
        <div className="settings-header">
          <h2>Profile Settings</h2>
          <p className="settings-subtitle">Update your personal information</p>
        </div>
        
        <form onSubmit={handleSave} className="settings-form">
          <div className="settings-field">
            <label htmlFor="name">Full Name</label>
            <input 
              id="name"
              type="text" 
              placeholder="Enter your full name" 
              value={name} 
              onChange={e => setName(e.target.value)} 
              required
            />
          </div>
          
          <div className="settings-field">
            <label htmlFor="email">Email Address</label>
            <input 
              id="email"
              type="email" 
              placeholder="Enter your email" 
              value={email} 
              onChange={e => setEmail(e.target.value)} 
              required
            />
          </div>

          {saved && (
            <div className="success-message">
              ✓ Profile updated successfully!
            </div>
          )}
          
          <button type="submit" className="settings-save-btn">
            Save Changes
          </button>
        </form>
      </div>
    </div>
  )
}
