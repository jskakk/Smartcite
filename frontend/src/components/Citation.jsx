import React, {useState, useEffect, useRef} from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
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
  const [isInitialized, setIsInitialized] = useState(false)
  const dropdownRef = useRef(null)
  const navigate = useNavigate()
  const location = useLocation()

  useEffect(()=>{
    // Check if we're editing a citation (coming from Dashboard edit button)
    if (location.state?.formData) {
      const formData = location.state.formData
      console.log('Loading form data from edit:', formData)
      setTitle(formData.title || '')
      setAuthor(formData.author || '')
      setPublisher(formData.publisher || '')
      setPublication(formData.publication || '')
      setYear(formData.year?.toString() || '')
      setUrl(formData.url || '')
      setStyleId(formData.styleId?.toString() || '')
      setSelectedStyle(formData.selectedStyle || 'Citation Style')
    } else {
      // Load saved form data from localStorage
      const savedFormData = localStorage.getItem('citationFormData')
      console.log('Loading saved form data:', savedFormData)
      if (savedFormData) {
        try {
          const formData = JSON.parse(savedFormData)
          console.log('Parsed form data:', formData)
          setTitle(formData.title || '')
          setAuthor(formData.author || '')
          setPublisher(formData.publisher || '')
          setPublication(formData.publication || '')
          setYear(formData.year || '')
          setUrl(formData.url || '')
          setStyleId(formData.styleId || '')
          setSelectedStyle(formData.selectedStyle || 'Citation Style')
        } catch (e) {
          console.error('Failed to load saved form data', e)
        }
      } else {
        console.log('No saved form data found in localStorage')
      }
    }
    
    // Mark as initialized so the save effect doesn't trigger on mount
    setIsInitialized(true)
    
    // Load citation styles
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

  // Save form data to localStorage whenever it changes (but only after initialization)
  useEffect(() => {
    if (!isInitialized) {
      setIsInitialized(true)
      return
    }
    
    const formData = {
      title,
      author,
      publisher,
      publication,
      year,
      url,
      styleId,
      selectedStyle
    }
    localStorage.setItem('citationFormData', JSON.stringify(formData))
  }, [title, author, publisher, publication, year, url, styleId, selectedStyle, isInitialized])

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

  const handleClear = (e) => {
    e.preventDefault()
    setTitle('')
    setAuthor('')
    setPublisher('')
    setPublication('')
    setYear('')
    setUrl('')
    setStyleId('')
    setSelectedStyle('Citation Style')
    localStorage.removeItem('citationFormData')
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
          
          <div className="citation-buttons">
            <button className="citation-btn" type="submit">Generate Citation</button>
            <button className="citation-btn-clear" type="button" onClick={handleClear}>Clear</button>
          </div>
        </form>
      </div>
    </div>
  )
}
