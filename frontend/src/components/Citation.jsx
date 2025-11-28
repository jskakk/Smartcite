import React, {useState, useEffect} from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../api'

export default function Citation(){
  const [title, setTitle] = useState('')
  const [author, setAuthor] = useState('')
  const [publisher, setPublisher] = useState('')
  const [year, setYear] = useState('')
  const [url, setUrl] = useState('')
  const [styles, setStyles] = useState([])
  const [styleId, setStyleId] = useState('')
  const navigate = useNavigate()

  useEffect(()=>{
    async function loadStyles(){
      try{
        const res = await api.get('/citation-styles')
        setStyles(res.data || [])
      }catch(e){ console.warn(e) }
    }
    loadStyles()
  },[])

  const handleSubmit = async (e) => {
    e.preventDefault()
    try{
      const payload = { title, author, publisher, publication: year, url, citationStyle: styleId }
      const res = await api.post('/citations', payload)
      if(res.status===200){
        navigate('/citation/result', { state: { citation: res.data } })
      }
    }catch(err){ alert('Create citation failed: '+err.message) }
  }

  return (
    <div className="card">
      <h2>Create Citation</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-field"><input placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} /></div>
        <div className="form-field"><input placeholder="Author" value={author} onChange={e=>setAuthor(e.target.value)} /></div>
        <div className="form-field"><input placeholder="Publisher" value={publisher} onChange={e=>setPublisher(e.target.value)} /></div>
        <div className="form-field" style={{display:'flex', gap:8}}>
          <input placeholder="Publication" value={year} onChange={e=>setYear(e.target.value)} />
          <input placeholder="URL (optional)" value={url} onChange={e=>setUrl(e.target.value)} />
        </div>
        <div className="form-field">
          <select value={styleId} onChange={e=>setStyleId(e.target.value)}>
            <option value="">Select Citation Style</option>
            {styles.map(s=> <option key={s.id} value={s.id}>{s.name}</option>)}
          </select>
        </div>
        <button className="btn" type="submit">Generate</button>
      </form>
    </div>
  )
}
