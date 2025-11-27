package com.appdevg6.smartcite.service.impl;

import com.appdevg6.smartcite.entity.Session;
import com.appdevg6.smartcite.entity.User;
import com.appdevg6.smartcite.repository.SessionRepository;
import com.appdevg6.smartcite.repository.UserRepository;
import com.appdevg6.smartcite.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {
    
    @Autowired
    private SessionRepository sessionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Session createSession(Long userViewId) {
        User user = userRepository.findById(userViewId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userViewId));
        Session session = new Session(user);
        return sessionRepository.save(session);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Session> getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Session> getSessionsByUser(Long userViewId) {
        return sessionRepository.findByUserViewId(userViewId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Session> getActiveSessionsByUser(Long userViewId) {
        User user = userRepository.findById(userViewId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userViewId));
        return sessionRepository.findByUserAndIsActiveTrue(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Session> getAllActiveSessions() {
        return sessionRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Session> getLatestSessionByUser(Long userViewId) {
        User user = userRepository.findById(userViewId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userViewId));
        return sessionRepository.findTopByUserOrderByStartTimeDesc(user);
    }
    
    @Override
    public Session endSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .map(session -> {
                    session.endSession();
                    return sessionRepository.save(session);
                })
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Session> getExpiredSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        return sessionRepository.findExpiredSessions(cutoffTime);
    }
    
    @Override
    public int endAllExpiredSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
        LocalDateTime endTime = LocalDateTime.now();
        return sessionRepository.endExpiredSessions(cutoffTime, endTime);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isUserActive(Long userViewId) {
        List<Session> activeSessions = getActiveSessionsByUser(userViewId);
        return !activeSessions.isEmpty();
    }
}