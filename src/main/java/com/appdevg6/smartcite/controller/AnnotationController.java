package com.appdevg6.smartcite.controller;

import com.appdevg6.smartcite.entity.Annotation;
import com.appdevg6.smartcite.service.AnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/annotations")
public class AnnotationController {

    @Autowired
    private AnnotationService annotationService;

    @PostMapping
    public Annotation createAnnotation(@RequestBody Annotation annotation) {
        return annotationService.createAnnotation(annotation);
    }

    @GetMapping
    public List<Annotation> getAllAnnotations() {
        return annotationService.getAllAnnotations();
    }

    @GetMapping("/grammarcheck/{checkId}")
    public List<Annotation> getAnnotationsByCheckId(@PathVariable Long checkId) {
        return annotationService.getAnnotationsByCheckId(checkId);
    }

    @PutMapping("/{id}")
    public Annotation updateAnnotation(@PathVariable Long id, @RequestBody Annotation annotation) {
        return annotationService.updateAnnotation(id, annotation);
    }

    @DeleteMapping("/{id}")
    public void deleteAnnotation(@PathVariable Long id) {
        annotationService.deleteAnnotation(id);
    }
}
