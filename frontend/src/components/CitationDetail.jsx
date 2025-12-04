import React, { useState, useEffect, useRef } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import api from '../api'
import './CitationDetail.css'

export default function CitationDetail() {
  const { state } = useLocation()
  const citation = state?.citation
  const [copied, setCopied] = useState(false)
  const [showEditModal, setShowEditModal] = useState(false)
  const navigate = useNavigate()
  
  // Form states
  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')
  const [publisher, setPublisher] = useState('')
  const [publication, setPublication] = useState('')
  const [year, setYear] = useState('')
  const [url, setUrl] = useState('')
  const [styles, setStyles] = useState([])
  const [styleId, setStyleId] = useState('')
  const [selectedStyle, setSelectedStyle] = useState('Citation Style')
  const [isDropdownOpen, setIsDropdownOpen] = useState(false)
  const dropdownRef = useRef(null)

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

  const handleDelete = () => {
    if (!citation) return
    if (confirm('Are you sure you want to delete this citation?')) {
      try {
        const storedUser = localStorage.getItem('user')
        if (!storedUser) return
        
        const user = JSON.parse(storedUser)
        const userId = user.userId || user.email
        const userCitationsKey = `citations_${userId}`
        
        const savedCitations = JSON.parse(localStorage.getItem(userCitationsKey) || '[]')
        const filtered = savedCitations.filter(c => c.citationId !== citation.citationId)
        
        localStorage.setItem(userCitationsKey, JSON.stringify(filtered))
        navigate('/dashboard')
      } catch (err) {
        console.error('Failed to delete citation:', err)
      }
    }
  }

  useEffect(() => {
    // Load citation styles
    async function loadStyles() {
      try {
        const res = await api.get('/citation-styles')
        let stylesData = res.data
        if (!Array.isArray(stylesData)) {
          stylesData = []
        }
        
        // Remove duplicate style names
        const uniqueStyles = stylesData.reduce((acc, current) => {
          const exists = acc.find(item => item.styleName === current.styleName)
          if (!exists) {
            acc.push(current)
          }
          return acc
        }, [])
        
        setStyles(uniqueStyles)
      } catch (e) {
        console.error('Error loading styles:', e)
        setStyles([])
      }
    }
    loadStyles()
  }, [])

  // Click outside handler for dropdown
  useEffect(() => {
    const handleClick = (e) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setIsDropdownOpen(false)
      }
    }

    if (isDropdownOpen) {
      document.addEventListener('click', handleClick)
      return () => document.removeEventListener('click', handleClick)
    }
  }, [isDropdownOpen])

  const handleEdit = () => {
    if (!citation) return
    // Pre-fill form with citation data
    setTitle(citation.title || '')
    setAuthor(citation.author || '')
    setPublisher(citation.publisher || '')
    setPublication(citation.publication || '')
    setYear(citation.year?.toString() || '')
    setUrl(citation.url || '')
    setStyleId(citation.style?.styleId?.toString() || '')
    setSelectedStyle(citation.style?.styleName || 'Citation Style')
    setShowEditModal(true)
  }

  const handleCloseModal = () => {
    setShowEditModal(false)
    setIsDropdownOpen(false)
  }

  const handleSelectStyle = (id, name) => {
    setStyleId(id)
    setSelectedStyle(name)
    setIsDropdownOpen(false)
  }

  const toggleDropdown = (e) => {
    e.preventDefault()
    e.stopPropagation()
    setIsDropdownOpen(!isDropdownOpen)
  }

  const handleSubmitEdit = async (e) => {
    e.preventDefault()
    try {
      const payload = {
        title,
        author,
        publisher,
        year: parseInt(year) || new Date().getFullYear(),
        url,
        style: styleId ? { styleId: parseInt(styleId) } : null
      }
      const res = await api.post('/citations', payload)
      if (res.data) {
        // Update the citation in localStorage
        const storedUser = localStorage.getItem('user')
        if (storedUser) {
          const user = JSON.parse(storedUser)
          const userId = user.userId || user.email
          const userCitationsKey = `citations_${userId}`
          
          const savedCitations = JSON.parse(localStorage.getItem(userCitationsKey) || '[]')
          const filtered = savedCitations.filter(c => c.citationId !== citation.citationId)
          filtered.unshift(res.data)
          
          localStorage.setItem(userCitationsKey, JSON.stringify(filtered.slice(0, 10)))
        }
        
        // Close modal and navigate to show updated citation
        handleCloseModal()
        navigate('/citation/detail', { state: { citation: res.data }, replace: true })
      }
    } catch (err) {
      alert('Update citation failed: ' + (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="citation-detail-container">
      <div className="citation-detail-box">
        <h2 className="citation-detail-title">Citation Details</h2>
        <div className="citation-detail-display">
          {!citation ? (
            <p className="no-citation">No citation found</p>
          ) : (
            <>
              <div className="citation-style-badge-detail">
                {citation.style?.styleName || 'N/A'}
              </div>
              <p className="citation-output-detail">
                {citation.generatedCitation || JSON.stringify(citation, null, 2)}
              </p>
            </>
          )}
        </div>
        <div className="citation-detail-actions">
          <button className="back-btn-detail" title="Back to Dashboard" onClick={() => navigate('/dashboard')}>
            ← Back
          </button>
          <div className="action-buttons-detail">
            <button 
              className="icon-btn-detail copy-btn-detail" 
              title={copied ? 'Copied!' : 'Copy citation'}
              onClick={handleCopy}
            >
              📋
            </button>
            <button 
              className="icon-btn-detail edit-btn-detail" 
              title="Edit citation"
              onClick={handleEdit}
            >
              ✏️
            </button>
            <button 
              className="icon-btn-detail delete-btn-detail" 
              title="Delete citation"
              onClick={handleDelete}
            >
              🗑️
            </button>
          </div>
        </div>
      </div>

      {/* Edit Modal */}
      {showEditModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>Edit Citation</h2>
              <button className="modal-close" onClick={handleCloseModal}>×</button>
            </div>
            <form onSubmit={handleSubmitEdit} className="modal-form">
              <div className="form-field">
                <input 
                  type="text" 
                  placeholder="Title" 
                  value={title} 
                  onChange={e => setTitle(e.target.value)} 
                  required
                />
              </div>
              
              <div className="form-field">
                <input 
                  type="text" 
                  placeholder="Author" 
                  value={author} 
                  onChange={e => setAuthor(e.target.value)} 
                  required
                />
              </div>
              
              <div className="form-field">
                <input 
                  type="text" 
                  placeholder="Publisher" 
                  value={publisher} 
                  onChange={e => setPublisher(e.target.value)} 
                />
              </div>
              
              <div className="form-row">
                <input 
                  type="text" 
                  placeholder="Publication" 
                  value={publication} 
                  onChange={e => setPublication(e.target.value)} 
                />
                <input 
                  type="number" 
                  placeholder="Year" 
                  value={year} 
                  onChange={e => setYear(e.target.value)} 
                />
              </div>
              
              <div className="form-field">
                <input 
                  type="url" 
                  placeholder="URL (optional)" 
                  value={url} 
                  onChange={e => setUrl(e.target.value)} 
                />
              </div>
              
              <div className="form-field">
                <div className="dropdown-wrapper-modal" ref={dropdownRef}>
                  <button
                    type="button"
                    className="dropdown-toggle-modal"
                    onClick={toggleDropdown}
                  >
                    {selectedStyle}
                  </button>
                  {isDropdownOpen && (
                    <div className="dropdown-menu-modal">
                      {Array.isArray(styles) && styles.length > 0 ? (
                        styles.map(s => (
                          <div
                            key={s.styleId}
                            className="dropdown-item-modal"
                            onClick={() => handleSelectStyle(s.styleId, s.styleName)}
                          >
                            {s.styleName}
                          </div>
                        ))
                      ) : (
                        <div className="dropdown-item-modal">No styles available</div>
                      )}
                    </div>
                  )}
                </div>
              </div>
              
              <div className="modal-buttons">
                <button type="button" className="modal-cancel" onClick={handleCloseModal}>
                  Cancel
                </button>
                <button type="submit" className="modal-save">
                  Update Citation
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
