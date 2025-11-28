import React from 'react'
import { useLocation } from 'react-router-dom'

export default function Result(){
  const { state } = useLocation()
  const citation = state?.citation
  return (
    <div className="card">
      <h2>Result</h2>
      <div style={{minHeight:120, background:'#fafafa', padding:12, borderRadius:6}}>{citation ? JSON.stringify(citation) : 'No result'}</div>
      <div style={{marginTop:12}}><button className="btn" onClick={()=>navigator.clipboard?.writeText(JSON.stringify(citation))}>COPY</button></div>
    </div>
  )
}
