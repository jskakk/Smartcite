package com.appdevg6.smartcite.repository;

import com.appdevg6.smartcite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    List<User> findByNameContainingIgnoreCase(String name);
    boolean existsByEmail(String email);
    
    @Query("SELECT DISTINCT u FROM User u JOIN u.sessions s WHERE s.isActive = true")
    List<User> findUsersWithActiveSessions();
}