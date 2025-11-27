package com.appdevg6.smartcite.service;

import com.appdevg6.smartcite.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    
    User createUser(User user);
    User createUser(String name, String email, String password);
    List<User> getAllUsers();
    Optional<User> getUserById(Long viewId);
    Optional<User> getUserByEmail(String email);
    List<User> searchUsersByName(String name);
    List<User> getUsersWithActiveSessions();
    User updateUser(Long viewId, User userDetails);
    User updateUserPassword(Long viewId, String newPassword);
    void deleteUser(Long viewId);
    boolean emailExists(String email);
    void startUserPosition(Long viewId);
}