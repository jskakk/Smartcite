import React, {useState} from 'react'
import api from '../api'
import './Grammar.css'

export default function Grammar(){
  const [text, setText] = useState('')
  const [correctedText, setCorrectedText] = useState('')
  const [copied, setCopied] = useState(false)

  const handleCheck = async () => {
    try{
      const payload = { inputText: text }
      const res = await api.post('/grammarchecks', payload)
      setCorrectedText(res.data?.correctedText || text)
    }catch(e){ 
      alert('Grammar check failed: '+(e.message||e)) 
    }
  }

  const handleCopy = async () => {
    try {
      await navigator.clipboard.writeText(correctedText)
      setCopied(true)
      setTimeout(() => setCopied(false), 2000)
    } catch (e) {
      console.error('Copy failed', e)
    }
  }

  return (
    <div className="grammar-container">
      <div className="grammar-card">
        <div className="grammar-header">
          <h2>Grammar Check</h2>
        </div>
        <div className="grammar-content">
          <div className="grammar-box">
            <textarea 
              rows={10} 
              value={text} 
              onChange={e=>setText(e.target.value)}
              placeholder="Enter your text here..."
            />
          </div>
          <div className="grammar-box result-box">
            <div className="corrected-text">
              {correctedText || 'Corrected text will appear here...'}
            </div>
          </div>
        </div>
        <div className="grammar-actions">
          <button className="grammar-btn check-btn" onClick={handleCheck}>Check Grammar</button>
          <button className="grammar-btn copy-btn" onClick={handleCopy}>
            {copied ? 'Copied!' : 'COPY'}
          </button>
        </div>
      </div>
    </div>
  )
}
