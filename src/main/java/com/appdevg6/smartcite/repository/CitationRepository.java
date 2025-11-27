package com.appdevg6.smartcite.repository;
 
import com.appdevg6.smartcite.entity.Citation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface CitationRepository extends JpaRepository<Citation, Long> {
    List<Citation> findByUser_ViewId(Long viewId);
}
 