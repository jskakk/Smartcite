package com.appdevg6.smartcite.service.impl;

import com.appdevg6.smartcite.entity.User;
import com.appdevg6.smartcite.repository.UserRepository;
import com.appdevg6.smartcite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }
        return userRepository.save(user);
    }
    
    @Override
    public User createUser(String name, String email, String password) {
        User user = new User(name, email, password);
        return createUser(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long viewId) {
        return userRepository.findById(viewId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithActiveSessions() {
        return userRepository.findUsersWithActiveSessions();
    }
    
    @Override
    public User updateUser(Long viewId, User userDetails) {
        return userRepository.findById(viewId)
                .map(existingUser -> {
                    if (!existingUser.getEmail().equals(userDetails.getEmail()) && 
                        userRepository.existsByEmail(userDetails.getEmail())) {
                        throw new RuntimeException("Email already exists: " + userDetails.getEmail());
                    }
                    existingUser.setName(userDetails.getName());
                    existingUser.setEmail(userDetails.getEmail());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + viewId));
    }
    
    @Override
    public User updateUserPassword(Long viewId, String newPassword) {
        return userRepository.findById(viewId)
                .map(user -> {
                    user.setPassword(newPassword);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + viewId));
    }
    
    @Override
    public void deleteUser(Long viewId) {
        if (!userRepository.existsById(viewId)) {
            throw new RuntimeException("User not found with id: " + viewId);
        }
        userRepository.deleteById(viewId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public void startUserPosition(Long viewId) {
        User user = userRepository.findById(viewId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + viewId));
        user.startPosition();
    }
}