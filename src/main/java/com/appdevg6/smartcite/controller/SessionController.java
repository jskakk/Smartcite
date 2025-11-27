package com.appdevg6.smartcite.controller;

import com.appdevg6.smartcite.entity.Session;
import com.appdevg6.smartcite.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {
    
    @Autowired
    private SessionService sessionService;
    
    @PostMapping("/user/{userViewId}")
    public ResponseEntity<?> createSession(@PathVariable Long userViewId) {
        try {
            Session session = sessionService.createSession(userViewId);
            return createSuccessResponse("Session created successfully", session, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllSessions() {
        try {
            List<Session> sessions = sessionService.getAllSessions();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", sessions.size());
            response.put("data", sessions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error retrieving sessions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionById(@PathVariable Long sessionId) {
        try {
            Optional<Session> session = sessionService.getSessionById(sessionId);
            if (session.isPresent()) {
                return createSuccessResponse("Session found", session.get(), HttpStatus.OK);
            } else {
                return createErrorResponse("Session not found with id: " + sessionId, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createErrorResponse("Error retrieving session: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userViewId}")
    public ResponseEntity<?> getSessionsByUser(@PathVariable Long userViewId) {
        try {
            List<Session> sessions = sessionService.getSessionsByUser(userViewId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", sessions.size());
            response.put("data", sessions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error retrieving user sessions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userViewId}/active")
    public ResponseEntity<?> getActiveSessionsByUser(@PathVariable Long userViewId) {
        try {
            List<Session> sessions = sessionService.getActiveSessionsByUser(userViewId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", sessions.size());
            response.put("data", sessions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error retrieving active sessions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveSessions() {
        try {
            List<Session> sessions = sessionService.getAllActiveSessions();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", sessions.size());
            response.put("data", sessions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error retrieving active sessions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userViewId}/latest")
    public ResponseEntity<?> getLatestSessionByUser(@PathVariable Long userViewId) {
        try {
            Optional<Session> session = sessionService.getLatestSessionByUser(userViewId);
            if (session.isPresent()) {
                return createSuccessResponse("Latest session found", session.get(), HttpStatus.OK);
            } else {
                return createErrorResponse("No sessions found for user: " + userViewId, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createErrorResponse("Error retrieving latest session: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PatchMapping("/{sessionId}/end")
    public ResponseEntity<?> endSession(@PathVariable Long sessionId) {
        try {
            Session endedSession = sessionService.endSession(sessionId);
            return createSuccessResponse("Session ended successfully", endedSession, HttpStatus.OK);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/expired")
    public ResponseEntity<?> getExpiredSessions() {
        try {
            List<Session> expiredSessions = sessionService.getExpiredSessions();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", expiredSessions.size());
            response.put("data", expiredSessions);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error retrieving expired sessions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/end-expired")
    public ResponseEntity<?> endAllExpiredSessions() {
        try {
            int endedCount = sessionService.endAllExpiredSessions();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ended " + endedCount + " expired sessions");
            response.put("endedCount", endedCount);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error ending expired sessions: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/user/{userViewId}/is-active")
    public ResponseEntity<?> isUserActive(@PathVariable Long userViewId) {
        try {
            boolean isActive = sessionService.isUserActive(userViewId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userViewId", userViewId);
            response.put("isActive", isActive);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error checking user activity: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    private ResponseEntity<Map<String, Object>> createSuccessResponse(String message, Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }
    
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, status);
    }
}