import React, {useState, useEffect, useRef} from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'
import './Citation.css'

export default function Citation(){
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
  const navigate = useNavigate()

  useEffect(()=>{
    async function loadStyles(){
      try{
        const res = await api.get('/citation-styles')
        console.log('Full API Response:', res)
        console.log('Response data:', res.data)
        console.log('Is array?', Array.isArray(res.data))
        
        let stylesData = res.data
        if (!Array.isArray(stylesData)) {
          console.warn('Response is not an array, type:', typeof stylesData)
          stylesData = []
        }
        
        // Remove duplicate style names, keep only the first occurrence
        const uniqueStyles = stylesData.reduce((acc, current) => {
          const exists = acc.find(item => item.styleName === current.styleName)
          if (!exists) {
            acc.push(current)
          }
          return acc
        }, [])
        
        console.log('Final styles data (duplicates removed):', uniqueStyles)
        setStyles(uniqueStyles)
      }catch(e){ 
        console.error('Error loading styles:', e)
        setStyles([])
      }
    }
    loadStyles()
  },[])

  // Simple click outside handler
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

  const handleSelectStyle = (id, name) => {
    console.log('Selected style:', id, name)
    setStyleId(id)
    setSelectedStyle(name)
    setIsDropdownOpen(false)
  }

  const toggleDropdown = (e) => {
    console.log('Toggle dropdown clicked')
    e.preventDefault()
    e.stopPropagation()
    setIsDropdownOpen(!isDropdownOpen)
  }

  const handleDropdownItemClick = (id, name) => {
    console.log('Dropdown item clicked:', id, name)
    handleSelectStyle(id, name)
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try{
      const payload = { 
        title, 
        author, 
        publisher, 
        year: parseInt(year) || new Date().getFullYear(),
        url,
        style: styleId ? { styleId: parseInt(styleId) } : null
      }
      const res = await api.post('/citations', payload)
      if(res.data){
        navigate('/citation/result', { state: { citation: res.data } })
      }
    }catch(err){ 
      alert('Create citation failed: '+ (err.response?.data?.message || err.message))
    }
  }

  return (
    <div className="citation-container">
      <div className="citation-card">
        <div className="citation-header">
          <h2>Citation Form</h2>
        </div>
        <form onSubmit={handleSubmit} className="citation-form">
          <div className="citation-field">
            <input 
              type="text" 
              placeholder="Title" 
              value={title} 
              onChange={e=>setTitle(e.target.value)} 
              required
            />
          </div>
          
          <div className="citation-field">
            <input 
              type="text" 
              placeholder="Author" 
              value={author} 
              onChange={e=>setAuthor(e.target.value)} 
              required
            />
          </div>
          
          <div className="citation-field">
            <input 
              type="text" 
              placeholder="Publisher" 
              value={publisher} 
              onChange={e=>setPublisher(e.target.value)} 
            />
          </div>
          
          <div className="citation-row">
            <input 
              type="text" 
              placeholder="Publication" 
              value={publication} 
              onChange={e=>setPublication(e.target.value)} 
            />
            <input 
              type="number" 
              placeholder="Year" 
              value={year} 
              onChange={e=>setYear(e.target.value)} 
            />
          </div>
          
          <div className="citation-field">
            <input 
              type="url" 
              placeholder="URL (optional)" 
              value={url} 
              onChange={e=>setUrl(e.target.value)} 
            />
          </div>
          
          <div className="citation-field">
            <div className="dropdown-wrapper" ref={dropdownRef}>
              <button
                type="button"
                className="dropdown-toggle"
                onClick={toggleDropdown}
              >
                {selectedStyle}
              </button>
              {isDropdownOpen && (
                <div className="dropdown-menu">
                  {Array.isArray(styles) && styles.length > 0 ? (
                    styles.map(s => (
                      <div
                        key={s.styleId}
                        className="dropdown-item"
                        onClick={() => handleDropdownItemClick(s.styleId, s.styleName)}
                      >
                        {s.styleName}
                      </div>
                    ))
                  ) : (
                    <div className="dropdown-item">No styles available</div>
                  )}
                </div>
              )}
            </div>
          </div>
          
          <button className="citation-btn" type="submit">Generate Citation</button>
        </form>
      </div>
    </div>
  )
}
