package com.appdevg6.smartcite.controller;
 
import com.appdevg6.smartcite.entity.Citation;
import com.appdevg6.smartcite.service.CitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/citations")
public class CitationController {
 
    @Autowired
    private CitationService citationService;
 
    @PostMapping
    public ResponseEntity<Citation> createCitation(@RequestBody Citation citation) {
        Citation created = citationService.createCitation(citation);
        return ResponseEntity.ok(created);
    }
 
    @GetMapping
    public ResponseEntity<List<Citation>> getAll() {
        return ResponseEntity.ok(citationService.getAllCitations());
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<Citation> getById(@PathVariable Long id) {
        return citationService.getCitationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
 
    @GetMapping("/user/{userViewId}")
    public ResponseEntity<List<Citation>> getByUser(@PathVariable Long userViewId) {
        return ResponseEntity.ok(citationService.getCitationsByUserId(userViewId));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        citationService.deleteCitation(id);
        return ResponseEntity.noContent().build();
    }
}
 