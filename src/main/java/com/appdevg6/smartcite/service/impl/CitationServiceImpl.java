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
 
        // Generate citation based on style
        citation.setGeneratedCitation(generateCitationString(citation));
        
        return citationRepository.save(citation);
    }
    
    private String generateCitationString(Citation citation) {
        String styleName = citation.getStyle() != null ? citation.getStyle().getStyleName() : "Unknown";
        String author = citation.getAuthor() != null ? citation.getAuthor() : "Unknown Author";
        String title = citation.getTitle() != null ? citation.getTitle() : "Unknown Title";
        String publisher = citation.getPublisher() != null ? citation.getPublisher() : "";
        String year = citation.getYear() != null ? citation.getYear().toString() : "";
        String url = citation.getUrl() != null ? citation.getUrl() : "";
        
        switch(styleName.toUpperCase()) {
            case "MLA":
                return generateMLA(author, title, publisher, year, url);
            case "APA":
                return generateAPA(author, title, publisher, year, url);
            case "CHICAGO":
                return generateChicago(author, title, publisher, year, url);
            case "IEEE":
                return generateIEEE(author, title, publisher, year, url);
            default:
                return author + ". " + title + ". " + publisher + ", " + year + ". " + url;
        }
    }
    
    private String generateMLA(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(author).append(". \"").append(title).append(".\"");
        if (!publisher.isEmpty()) sb.append(" ").append(publisher);
        if (!year.isEmpty()) sb.append(", ").append(year);
        if (!url.isEmpty()) sb.append(". ").append(url);
        sb.append(".");
        return sb.toString();
    }
    
    private String generateAPA(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(author).append(" (").append(year).append("). ");
        sb.append(title).append(". ");
        if (!publisher.isEmpty()) sb.append(publisher).append(".");
        if (!url.isEmpty()) sb.append(" Retrieved from ").append(url);
        return sb.toString();
    }
    
    private String generateChicago(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append(author).append(". ").append(title).append(". ");
        if (!publisher.isEmpty()) sb.append(publisher);
        if (!year.isEmpty()) sb.append(", ").append(year);
        if (!url.isEmpty()) sb.append(". ").append(url);
        sb.append(".");
        return sb.toString();
    }
    
    private String generateIEEE(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("[1] ").append(author).append(", \"").append(title).append(",\" ");
        if (!publisher.isEmpty()) sb.append(publisher).append(", ");
        if (!year.isEmpty()) sb.append(year);
        if (!url.isEmpty()) sb.append(". Available: ").append(url);
        sb.append(".");
        return sb.toString();
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