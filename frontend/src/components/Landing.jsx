import React from 'react'
import { Link } from 'react-router-dom'
import './Landing.css'

export default function Landing(){
  return (
    <div className="landing-container">
      <div className="landing-card">
        <div className="landing-icon">📚</div>
        <h1 className="landing-title">Welcome to SmartCite</h1>
        <p className="landing-subtitle">Your intelligent citation and grammar companion</p>
        <Link to="/introduction"><button className="landing-btn">Get Started</button></Link>
        <div className="landing-features">
          <div className="feature-badge">✓ Multiple Citation Styles</div>
          <div className="feature-badge">✓ Grammar Checking</div>
          <div className="feature-badge">✓ Fast & Accurate</div>
        </div>
      </div>
    </div>
  )
}
