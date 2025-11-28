import React, {useState} from 'react'
import api from '../api'

export default function Grammar(){
  const [text, setText] = useState('')
  const [result, setResult] = useState(null)

  const handleCheck = async () => {
    try{
      const payload = { originalText: text }
      const res = await api.post('/grammarchecks', payload)
      setResult(res.data)
    }catch(e){ alert('Grammar check failed: '+(e.message||e)) }
  }

  return (
    <div className="card">
      <h2>Grammar Check</h2>
      <div className="form-field"><textarea rows={6} value={text} onChange={e=>setText(e.target.value)} /></div>
      <button className="btn" onClick={handleCheck}>Check</button>
      {result && (
        <div style={{marginTop:12, background:'#fafafa', padding:12, borderRadius:6}}>
          <pre>{JSON.stringify(result, null, 2)}</pre>
        </div>
      )}
    </div>
  )
}
