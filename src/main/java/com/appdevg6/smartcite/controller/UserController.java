package com.appdevg6.smartcite.controller;

import com.appdevg6.smartcite.entity.User;
import com.appdevg6.smartcite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return createSuccessResponse("User created successfully", createdUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", users.size());
            response.put("data", users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error retrieving users: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{viewId}")
    public ResponseEntity<?> getUserById(@PathVariable Long viewId) {
        try {
            Optional<User> user = userService.getUserById(viewId);
            if (user.isPresent()) {
                return createSuccessResponse("User found", user.get(), HttpStatus.OK);
            } else {
                return createErrorResponse("User not found with id: " + viewId, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createErrorResponse("Error retrieving user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        try {
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isPresent()) {
                return createSuccessResponse("User found", user.get(), HttpStatus.OK);
            } else {
                return createErrorResponse("User not found with email: " + email, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createErrorResponse("Error retrieving user: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchUsersByName(@RequestParam String name) {
        try {
            List<User> users = userService.searchUsersByName(name);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", users.size());
            response.put("data", users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error searching users: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{viewId}")
    public ResponseEntity<?> updateUser(@PathVariable Long viewId, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(viewId, userDetails);
            return createSuccessResponse("User updated successfully", updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PatchMapping("/{viewId}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long viewId, @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("password");
            User updatedUser = userService.updateUserPassword(viewId, newPassword);
            return createSuccessResponse("Password updated successfully", updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{viewId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long viewId) {
        try {
            userService.deleteUser(viewId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{viewId}/start-position")
    public ResponseEntity<?> startUserPosition(@PathVariable Long viewId) {
        try {
            userService.startUserPosition(viewId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User position started successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = userService.emailExists(email);
            Map<String, Object> response = new HashMap<>();
            response.put("exists", exists);
            response.put("email", email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return createErrorResponse("Error checking email: " + e.getMessage(), HttpStatus.BAD_REQUEST);
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