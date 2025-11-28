import React from 'react'
import { Link } from 'react-router-dom'

export default function Landing(){
  return (
    <div className="card">
      <h1>Welcome to SmartCite</h1>
      <div style={{height:80, background:'#eee', borderRadius:6, margin:'12px 0'}}></div>
      <Link to="/register"><button className="btn">Get Started</button></Link>
    </div>
  )
}
