package com.appdevg6.smartcite.service;
 
import com.appdevg6.smartcite.entity.CitationStyle;
import java.util.List;
import java.util.Optional;
 
public interface CitationStyleService {
    CitationStyle createStyle(CitationStyle style);
    Optional<CitationStyle> getStyleById(Long id);
    List<CitationStyle> getAllStyles();
    void deleteStyle(Long id);
}
 