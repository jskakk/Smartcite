package com.appdevg6.smartcite.service;

import com.appdevg6.smartcite.entity.Annotation;
import java.util.List;

public interface AnnotationService {

    Annotation createAnnotation(Annotation annotation);

    List<Annotation> getAllAnnotations();

    List<Annotation> getAnnotationsByCheckId(Long checkId);

    Annotation updateAnnotation(Long id, Annotation updated);

    void deleteAnnotation(Long id);
}
