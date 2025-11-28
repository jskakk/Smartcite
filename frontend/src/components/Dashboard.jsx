import React from 'react'

export default function Dashboard(){
  return (
    <div className="card">
      <h2>Welcome back, User</h2>
      <p>Use the tabs above to create citations, run grammar checks, and manage settings.</p>
      <div style={{display:'flex', gap:12, marginTop:16}}>
        <div style={{width:80, height:60, background:'#f0f4ff', borderRadius:6}}></div>
        <div style={{width:80, height:60, background:'#f0f4ff', borderRadius:6}}></div>
        <div style={{width:80, height:60, background:'#f0f4ff', borderRadius:6}}></div>
        <div style={{width:80, height:60, background:'#f0f4ff', borderRadius:6}}></div>
      </div>
    </div>
  )
}
