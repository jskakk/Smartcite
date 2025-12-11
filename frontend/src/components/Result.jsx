import React, { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import './Result.css'

export default function Result() {
  const { state } = useLocation()
  const citation = state?.citation
  const [copied, setCopied] = useState(false)
  const [saved, setSaved] = useState(false)
  const navigate = useNavigate()

  const handleCopy = async () => {
    if (!citation) return
    try {
      const textToCopy = citation.generatedCitation || JSON.stringify(citation)
      await navigator.clipboard.writeText(textToCopy)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    } catch (e) {
      console.error('Copy failed', e)
    }
  }

  const handleSave = () => {
    if (!citation) return
    
    // Get current user
    const storedUser = localStorage.getItem('user')
    if (!storedUser) {
      alert('Please log in to save citations')
      return
    }
    
    const user = JSON.parse(storedUser)
    const userId = user.userId || user.email
    const userCitationsKey = `citations_${userId}`
    
    // Get existing saved citations for this user
    const savedCitations = JSON.parse(localStorage.getItem(userCitationsKey) || '[]')
    
    // Check if citation already exists (by citationId)
    const exists = savedCitations.some(c => c.citationId === citation.citationId)
    
    if (!exists) {
      // Add the new citation to the beginning of the array
      savedCitations.unshift(citation)
      
      // Keep only the 10 most recent citations
      const limitedCitations = savedCitations.slice(0, 10)
      
      // Save back to localStorage with user-specific key
      localStorage.setItem(userCitationsKey, JSON.stringify(limitedCitations))
      
      setSaved(true)
      setTimeout(() => {
        setSaved(false)
        // Navigate to dashboard to show the saved citation
        navigate('/dashboard')
      }, 1000)
    } else {
      alert('This citation is already saved!')
    }
  }

  const formatCitation = (citationText, style) => {
    if (!citationText) return citationText
    
    // For all formats, we need to italicize the book title
    // Pattern varies by format
    const parts = citationText.split('. ')
    
    if (style === 'MLA' && parts.length >= 3) {
      // MLA: Author. Title. Publisher, year. URL
      // parts[0] = Author, parts[1] = Title, rest = Publisher, year, URL
      return (
        <>
          {parts[0]}. <em>{parts[1]}</em>. {parts.slice(2).join('. ')}
        </>
      )
    } else if (style === 'APA' && parts.length >= 3) {
      // APA: Author (Year). Title. Publisher. URL
      // parts[0] = Author (Year), parts[1] = Title, rest = Publisher, URL
      return (
        <>
          {parts[0]}. <em>{parts[1]}</em>. {parts.slice(2).join('. ')}
        </>
      )
    } else if (style === 'Chicago' && parts.length >= 3) {
      // Chicago: Author. Title. Place: Publisher, year. URL
      // parts[0] = Author, parts[1] = Title, rest = Place: Publisher, year, URL
      return (
        <>
          {parts[0]}. <em>{parts[1]}</em>. {parts.slice(2).join('. ')}
        </>
      )
    } else if (style === 'IEEE' && parts.length >= 2) {
      // IEEE: M. L. Santos, Title. Publisher, year. [Online]. Available: URL
      const firstPart = parts[0] // Author, Title
      const titleStart = firstPart.indexOf(', ') + 2
      const author = firstPart.substring(0, titleStart - 2)
      const title = firstPart.substring(titleStart)
      return (
        <>
          {author}, <em>{title}</em>. {parts.slice(1).join('. ')}
        </>
      )
    }
    
    return citationText
  }

  return (
    <div className="result-container">
      <div className="result-box">
        <h2 className="result-title">Result</h2>
        <div className="result-display">
          {!citation ? (
            <p className="no-result">No citation generated yet</p>
          ) : (
            <p className="citation-output">
              {formatCitation(
                citation.generatedCitation || JSON.stringify(citation, null, 2),
                citation.style?.styleName
              )}
            </p>
          )}
        </div>
        <div className="result-actions">
          <button className="back-btn" title="Back" onClick={() => navigate('/citation')}>← Back</button>
          <div className="action-buttons">
            <button 
              className="icon-btn copy-btn" 
              title={copied ? 'Copied!' : 'Copy citation'}
              onClick={handleCopy}
            >
              📋
            </button>
            <button 
              className="icon-btn save-btn" 
              title={saved ? 'Saved!' : 'Save citation'}
              onClick={handleSave}
            >
              💾
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
