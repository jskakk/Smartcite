package com.appdevg6.smartcite.repository;

import com.appdevg6.smartcite.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnnotationRepository extends JpaRepository<Annotation, Long> {
    List<Annotation> findByGrammarCheck_CheckId(Long checkId);
}
