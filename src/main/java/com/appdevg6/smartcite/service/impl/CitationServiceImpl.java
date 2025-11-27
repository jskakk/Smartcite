package com.appdevg6.smartcite.service.impl;
 
import com.appdevg6.smartcite.entity.Citation;
import com.appdevg6.smartcite.entity.CitationStyle;
import com.appdevg6.smartcite.entity.Session;
import com.appdevg6.smartcite.entity.User;
import com.appdevg6.smartcite.repository.CitationRepository;
import com.appdevg6.smartcite.repository.CitationStyleRepository;
import com.appdevg6.smartcite.repository.SessionRepository;
import com.appdevg6.smartcite.repository.UserRepository;
import com.appdevg6.smartcite.service.CitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
import java.util.Optional;
 
@Service
@Transactional
public class CitationServiceImpl implements CitationService {
 
    @Autowired
    private CitationRepository citationRepository;
 
    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private SessionRepository sessionRepository;
 
    @Autowired
    private CitationStyleRepository styleRepository;
 
    @Override
    public Citation createCitation(Citation citation) {
        // If user id was provided, fetch and set managed user
        if (citation.getUser() != null && citation.getUser().getViewId() != null) {
            User user = userRepository.findById(citation.getUser().getViewId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            citation.setUser(user);
        }
 
        if (citation.getSession() != null && citation.getSession().getSessionId() != null) {
            Session session = sessionRepository.findById(citation.getSession().getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            citation.setSession(session);
        }
 
        if (citation.getStyle() != null && citation.getStyle().getStyleId() != null) {
            CitationStyle style = styleRepository.findById(citation.getStyle().getStyleId())
                    .orElseThrow(() -> new RuntimeException("Citation style not found"));
            citation.setStyle(style);
        }
 
        return citationRepository.save(citation);
    }
 
    @Override
    @Transactional(readOnly = true)
    public Optional<Citation> getCitationById(Long id) {
        return citationRepository.findById(id);
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<Citation> getAllCitations() {
        return citationRepository.findAll();
    }
 
    @Override
    @Transactional(readOnly = true)
    public List<Citation> getCitationsByUserId(Long userViewId) {
        return citationRepository.findByUser_ViewId(userViewId);
    }
 
    @Override
    public void deleteCitation(Long id) {
        if (!citationRepository.existsById(id)) {
            throw new RuntimeException("Citation not found with id: " + id);
        }
        citationRepository.deleteById(id);
    }
}