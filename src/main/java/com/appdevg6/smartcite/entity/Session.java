package com.appdevg6.smartcite.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sessions")
public class Session {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long sessionId;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_view_id")
    @JsonIgnore 
    private User user;
    
    public Session() {
        this.startTime = LocalDateTime.now();
        this.isActive = true;
    }
    
    public Session(User user) {
        this();
        this.user = user;
    }
    
    public void endSession() {
        this.endTime = LocalDateTime.now();
        this.isActive = false;
    }
    
    public boolean isSessionActive() {
        return this.isActive && this.endTime == null;
    }
    
    // Getters and Setters
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}