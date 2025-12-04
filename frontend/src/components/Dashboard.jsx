import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'
import './Dashboard.css'

export default function Dashboard(){
  const [userName, setUserName] = useState('User')
  const [citations, setCitations] = useState([])
  const navigate = useNavigate()

  useEffect(() => {
    // Get user from localStorage
    const storedUser = localStorage.getItem('user')
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser)
        setUserName(user.name || 'User')
      } catch (e) {
        console.error('Failed to parse user data', e)
      }
    }

    // Fetch recent citations
    loadCitations()
  }, [])

  const loadCitations = async () => {
    try {
      const res = await api.get('/citations')
      if (res.data && Array.isArray(res.data)) {
        // Get the 4 most recent citations
        setCitations(res.data.slice(0, 4))
      }
    } catch (err) {
      console.error('Failed to load citations:', err)
    }
  }

  const handleCitationClick = (citation) => {
    navigate('/citation/result', { state: { citation } })
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
