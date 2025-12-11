import React, { useState, useEffect } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import api from '../api'
import './Dashboard.css'

export default function Dashboard(){
  const [userName, setUserName] = useState('User')
  const [citations, setCitations] = useState([])
  const navigate = useNavigate()
  const location = useLocation()

  useEffect(() => {
    // Function to update user name
    const updateUserName = () => {
      const storedUser = localStorage.getItem('user')
      if (storedUser) {
        try {
          const user = JSON.parse(storedUser)
          setUserName(user.name || 'User')
        } catch (e) {
          console.error('Failed to parse user data', e)
        }
      }
    }

    // Initial load
    updateUserName()
    loadCitations()

    // Listen for storage events (triggered when Settings updates user data)
    window.addEventListener('storage', updateUserName)
    
    return () => {
      window.removeEventListener('storage', updateUserName)
    }
  }, [location])

  const loadCitations = async () => {
    try {
      // Get current user
      const storedUser = localStorage.getItem('user')
      if (!storedUser) {
        setCitations([])
        return
      }
      
      const user = JSON.parse(storedUser)
      const userId = user.userId || user.email
      
      // Load saved citations for this specific user
      const userCitationsKey = `citations_${userId}`
      const savedCitations = JSON.parse(localStorage.getItem(userCitationsKey) || '[]')
      
      // Get the 4 most recent citations
      setCitations(savedCitations.slice(0, 4))
    } catch (err) {
      console.error('Failed to load citations:', err)
      setCitations([])
    }
  }

  const handleCitationClick = (citation) => {
    navigate('/citation/detail', { state: { citation } })
  }

  const handleDelete = (e, citationId) => {
    e.stopPropagation()
    if (confirm('Are you sure you want to delete this citation?')) {
      try {
        const storedUser = localStorage.getItem('user')
        if (!storedUser) return
        
        const user = JSON.parse(storedUser)
        const userId = user.userId || user.email
        const userCitationsKey = `citations_${userId}`
        
        const savedCitations = JSON.parse(localStorage.getItem(userCitationsKey) || '[]')
        const filtered = savedCitations.filter(c => c.citationId !== citationId)
        
        localStorage.setItem(userCitationsKey, JSON.stringify(filtered))
        setCitations(filtered.slice(0, 4))
      } catch (err) {
        console.error('Failed to delete citation:', err)
      }
    }
  }

  const handleEdit = (e, citation) => {
    e.stopPropagation()
    // Navigate to Citation form and pre-fill with citation data
    navigate('/citation', { 
      state: { 
        editingCitation: citation,
        formData: {
          title: citation.title,
          author: citation.author,
          publisher: citation.publisher,
          publication: citation.publication,
          year: citation.year,
          url: citation.url,
          styleId: citation.style?.styleId,
          selectedStyle: citation.style?.styleName
        }
      }
    })
  }

  return (
    <div className="dashboard-container">
      <div className="welcome-card">
        <h2 className="welcome-title">Welcome back, {userName}</h2>
        <p className="welcome-subtitle">Time to craft citations with style — because your references deserve a glow-up too!</p>
      </div>

      <div className="citations-section">
        <h3 className="section-title">Recent Citations</h3>
        <div className="feature-cards">
          {citations.length > 0 ? (
            citations.map((citation) => (
              <div 
                key={citation.citationId} 
                className="citation-card"
                onClick={() => handleCitationClick(citation)}
              >
                <div className="citation-style-badge">
                  {citation.style?.styleName || 'N/A'}
                </div>
                <h4 className="citation-title">{citation.title}</h4>
                <p className="citation-author">{citation.author}</p>
                <div className="citation-meta">
                  <span>{citation.year}</span>
                  {citation.publisher && <span> • {citation.publisher}</span>}
                </div>
              </div>
            ))
          ) : (
            <>
              <div className="empty-card">
                <div className="empty-icon">📝</div>
                <p>No citations yet</p>
              </div>
              <div className="empty-card">
                <div className="empty-icon">✨</div>
                <p>Create your first citation</p>
              </div>
              <div className="empty-card">
                <div className="empty-icon">🎯</div>
                <p>Start now!</p>
              </div>
              <div className="empty-card">
                <div className="empty-icon">🚀</div>
                <p>Get started</p>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  )
}
