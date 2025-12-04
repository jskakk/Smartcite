import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import './Introduction.css'

export default function Introduction(){
  const [isVisible, setIsVisible] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    setIsVisible(true)
  }, [])

  const handleGetStarted = () => {
    navigate('/register')
  }

  return (
    <div className={`intro-container ${isVisible ? 'visible' : ''}`}>
      <div className="intro-content">
        <div className="intro-header">
          <h1 className="intro-title">About SmartCite</h1>
          <div className="title-underline"></div>
        </div>

        <div className="intro-sections">
          <section className="intro-section fade-in-1">
            <div className="section-icon">📚</div>
            <h2>Citation Generation</h2>
            <p>Effortlessly generate citations in multiple formats including MLA, APA, Chicago, and IEEE styles. No more manual formatting headaches!</p>
          </section>

          <section className="intro-section fade-in-2">
            <div className="section-icon">✏️</div>
            <h2>Grammar Checking</h2>
            <p>Our advanced grammar checking tool helps you maintain professional writing standards. Catch errors and improve your writing quality instantly.</p>
          </section>

          <section className="intro-section fade-in-3">
            <div className="section-icon">⚡</div>
            <h2>Fast & Reliable</h2>
            <p>Quick processing and accurate results. SmartCite is designed for speed and reliability, saving you time on your academic work.</p>
          </section>

          <section className="intro-section fade-in-4">
            <div className="section-icon">🔒</div>
            <h2>Secure & Private</h2>
            <p>Your data is safe with us. We prioritize your privacy and security with encrypted connections and secure data handling.</p>
          </section>
        </div>

        <div className="intro-benefits">
          <h2 className="benefits-title fade-in-5">Why Choose SmartCite?</h2>
          <ul className="benefits-list fade-in-6">
            <li>✓ Support for 4+ citation styles</li>
            <li>✓ Real-time grammar checking</li>
            <li>✓ User-friendly interface</li>
            <li>✓ Free and easy to use</li>
            <li>✓ 24/7 availability</li>
          </ul>
        </div>

        <div className="intro-cta fade-in-7">
          <button className="intro-btn primary" onClick={handleGetStarted}>Get Started Now</button>
          <button className="intro-btn secondary" onClick={() => navigate('/')}>Back to Home</button>
        </div>
      </div>

      <div className="background-decoration">
        <div className="decoration-circle circle-1"></div>
        <div className="decoration-circle circle-2"></div>
        <div className="decoration-circle circle-3"></div>
      </div>
    </div>
  )
}
