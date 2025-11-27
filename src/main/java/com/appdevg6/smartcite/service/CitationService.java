package com.appdevg6.smartcite.service;
 
import com.appdevg6.smartcite.entity.Citation;
 
import java.util.List;
import java.util.Optional;
 
public interface CitationService {
    Citation createCitation(Citation citation);
    Optional<Citation> getCitationById(Long id);
    List<Citation> getAllCitations();
    List<Citation> getCitationsByUserId(Long userViewId);
    void deleteCitation(Long id);
}
 