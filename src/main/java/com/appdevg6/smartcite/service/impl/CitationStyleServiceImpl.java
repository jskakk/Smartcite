package com.appdevg6.smartcite.service.impl;
 
import com.appdevg6.smartcite.entity.CitationStyle;
import com.appdevg6.smartcite.repository.CitationStyleRepository;
import com.appdevg6.smartcite.service.CitationStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.Optional;
 
@Service
@Transactional
public class CitationStyleServiceImpl implements CitationStyleService {
 
    @Autowired
    private CitationStyleRepository styleRepository;
 
    @Override
    public CitationStyle createStyle(CitationStyle style) {
        return styleRepository.save(style);
    }
 
    @Override
    @Transactional(readOnly = true)
    public Optional<CitationStyle> getStyleById(Long id) {
        return styleRepository.findById(id);
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<CitationStyle> getAllStyles() {
        return styleRepository.findAll();
    }
 
    @Override
    public void deleteStyle(Long id) {
        if (!styleRepository.existsById(id)) {
            throw new RuntimeException("Citation style not found: " + id);
        }
        styleRepository.deleteById(id);
    }
}