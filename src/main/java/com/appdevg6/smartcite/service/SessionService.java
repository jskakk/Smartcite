package com.appdevg6.smartcite.service;

import com.appdevg6.smartcite.entity.Session;
import java.util.List;
import java.util.Optional;

public interface SessionService {
    
    Session createSession(Long userViewId);
    List<Session> getAllSessions();
    Optional<Session> getSessionById(Long sessionId);
    List<Session> getSessionsByUser(Long userViewId);
    List<Session> getActiveSessionsByUser(Long userViewId);
    List<Session> getAllActiveSessions();
    Optional<Session> getLatestSessionByUser(Long userViewId);
    Session endSession(Long sessionId);
    List<Session> getExpiredSessions();
    int endAllExpiredSessions();
    boolean isUserActive(Long userViewId);
}