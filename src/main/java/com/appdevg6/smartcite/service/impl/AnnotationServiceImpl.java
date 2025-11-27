package com.appdevg6.smartcite.service.impl;

import com.appdevg6.smartcite.entity.Annotation;
import com.appdevg6.smartcite.repository.AnnotationRepository;
import com.appdevg6.smartcite.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Autowired
    private AnnotationRepository annotationRepository;

    @Override
    public Annotation createAnnotation(Annotation annotation) {
        return annotationRepository.save(annotation);
    }

    @Override
    public List<Annotation> getAllAnnotations() {
        return annotationRepository.findAll();
    }

    @Override
    public List<Annotation> getAnnotationsByCheckId(Long checkId) {
        return annotationRepository.findByGrammarCheck_CheckId(checkId);
    }

    @Override
    public Annotation updateAnnotation(Long id, Annotation updated) {
        Annotation existing = annotationRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setErrorType(updated.getErrorType());
            existing.setSuggestion(updated.getSuggestion());
            existing.setComment(updated.getComment());
            existing.setSeverity(updated.getSeverity());
            return annotationRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteAnnotation(Long id) {
        annotationRepository.deleteById(id);
    }
}
