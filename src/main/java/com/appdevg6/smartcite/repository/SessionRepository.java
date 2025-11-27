package com.appdevg6.smartcite.repository;

import com.appdevg6.smartcite.entity.Session;
import com.appdevg6.smartcite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    List<Session> findByUser(User user);
    List<Session> findByUserViewId(Long userViewId);
    List<Session> findByUserAndIsActiveTrue(User user);
    List<Session> findByIsActiveTrue();
    Optional<Session> findTopByUserOrderByStartTimeDesc(User user);
    
    @Query("SELECT s FROM Session s WHERE s.isActive = true AND s.startTime < :cutoffTime")
    List<Session> findExpiredSessions(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    @Modifying
    @Query("UPDATE Session s SET s.isActive = false, s.endTime = :endTime WHERE s.isActive = true AND s.startTime < :cutoffTime")
    int endExpiredSessions(@Param("cutoffTime") LocalDateTime cutoffTime, @Param("endTime") LocalDateTime endTime);
}