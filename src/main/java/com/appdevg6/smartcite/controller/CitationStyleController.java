package com.appdevg6.smartcite.controller;
 
import com.appdevg6.smartcite.entity.CitationStyle;
import com.appdevg6.smartcite.service.CitationStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/citation-styles")
public class CitationStyleController {
 
    @Autowired
    private CitationStyleService styleService;
 
    @PostMapping
    public ResponseEntity<CitationStyle> createStyle(@RequestBody CitationStyle style) {
        CitationStyle created = styleService.createStyle(style);
        return ResponseEntity.ok(created);
    }
 
    @GetMapping
    public ResponseEntity<List<CitationStyle>> getAll() {
        return ResponseEntity.ok(styleService.getAllStyles());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<CitationStyle> getById(@PathVariable Long id) {
        return styleService.getStyleById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        styleService.deleteStyle(id);
        return ResponseEntity.noContent().build();
    }
}