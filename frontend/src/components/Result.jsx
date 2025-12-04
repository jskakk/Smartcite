import React, { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import './Result.css'

export default function Result() {
  const { state } = useLocation()
  const citation = state?.citation
  const [copied, setCopied] = useState(false)
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

  const handleDownload = () => {
    if (!citation) return
    const textToDownload = citation.generatedCitation || JSON.stringify(citation, null, 2)
    const blob = new Blob([textToDownload], { type: 'text/plain' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = 'citation.txt'
    link.click()
    URL.revokeObjectURL(url)
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
              {citation.generatedCitation || JSON.stringify(citation, null, 2)}
            </p>
          )}
        </div>
        <div className="result-actions">
          <button className="back-btn" onClick={() => navigate('/citation')}>Back</button>
          <div className="action-buttons">
            <button className="result-btn copy-btn" onClick={handleCopy}>
              {copied ? 'Copied!' : 'COPY'}
            </button>
            <button className="result-btn download-btn" onClick={handleDownload}>
              ↓
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
