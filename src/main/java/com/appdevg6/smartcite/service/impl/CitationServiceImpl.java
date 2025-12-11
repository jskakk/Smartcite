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
        String publicationPlace = citation.getPublicationPlace() != null ? citation.getPublicationPlace() : "";
        
        switch(styleName.toUpperCase()) {
            case "MLA":
                return generateMLA(author, title, publisher, year, url);
            case "APA":
                return generateAPA(author, title, publisher, year, url);
            case "CHICAGO":
                return generateChicago(author, title, publisher, year, url, publicationPlace);
            case "IEEE":
                return generateIEEE(author, title, publisher, year, url);
            default:
                return author + ". " + title + ". " + publisher + ", " + year + ". " + url;
        }
    }
    
    private String generateMLA(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        
        // Reverse author name (Last, First format)
        String reversedAuthor = reverseAuthorName(author);
        
        // Author in Last, First format
        sb.append(reversedAuthor);
        // Only add period if author doesn't already end with one
        if (!reversedAuthor.endsWith(".")) {
            sb.append(".");
        }
        sb.append(" ");
        
        // Book title (will be styled as italic in frontend)
        sb.append(title).append(". ");
        
        // Publisher
        if (!publisher.isEmpty()) sb.append(publisher);
        
        // Year
        if (!year.isEmpty()) sb.append(", ").append(year);
        
        // URL if provided
        if (!url.isEmpty()) sb.append(". ").append(url);
        
        sb.append(".");
        return sb.toString();
    }
    
    private String reverseAuthorName(String author) {
        // Split by space to get parts
        String[] parts = author.trim().split("\\s+");
        if (parts.length < 2) {
            return author; // Return as-is if not enough parts
        }
        
        // Last part is last name, rest is first/middle
        String lastName = parts[parts.length - 1];
        StringBuilder firstMiddle = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (i > 0) firstMiddle.append(" ");
            firstMiddle.append(parts[i]);
        }
        
        return lastName + ", " + firstMiddle.toString();
    }
    
    private String generateAPA(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        
        // Reverse author name with initials (Last, F. M.)
        String reversedAuthor = reverseAuthorNameWithInitials(author);
        
        // Author (Year).
        sb.append(reversedAuthor).append(" (").append(year).append("). ");
        
        // Title in sentence case
        String sentenceCaseTitle = toSentenceCase(title);
        sb.append(sentenceCaseTitle).append(". ");
        
        // Publisher
        if (!publisher.isEmpty()) {
            sb.append(publisher);
            if (!publisher.endsWith(".")) sb.append(".");
        }
        
        // URL if provided
        if (!url.isEmpty()) sb.append(" ").append(url);
        
        return sb.toString();
    }
    
    private String reverseAuthorNameWithInitials(String author) {
        String[] parts = author.trim().split("\\s+");
        if (parts.length < 2) {
            return author;
        }
        
        // Last part is last name
        String lastName = parts[parts.length - 1];
        
        // Convert first/middle names to initials
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (part.length() > 0) {
                // Get first character
                char initial = part.charAt(0);
                if (i > 0) initials.append(" ");
                initials.append(initial).append(".");
            }
        }
        
        return lastName + ", " + initials.toString();
    }
    
    private String toSentenceCase(String text) {
        if (text == null || text.isEmpty()) return text;
        
        // Convert to lowercase first
        String lower = text.toLowerCase();
        
        // Capitalize first letter
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }
    
    private String generateChicago(String author, String title, String publisher, String year, String url, String publicationPlace) {
        StringBuilder sb = new StringBuilder();
        
        // Reverse author name (Last, First format)
        String reversedAuthor = reverseAuthorName(author);
        
        // Author
        sb.append(reversedAuthor);
        if (!reversedAuthor.endsWith(".")) {
            sb.append(".");
        }
        sb.append(" ");
        
        // Title (will be italicized in frontend)
        sb.append(title).append(". ");
        
        // Publication place and publisher
        if (!publicationPlace.isEmpty()) {
            sb.append(publicationPlace).append(": ");
        }
        if (!publisher.isEmpty()) {
            sb.append(publisher);
        }
        
        // Year
        if (!year.isEmpty()) sb.append(", ").append(year);
        
        // URL if provided
        if (!url.isEmpty()) sb.append(". ").append(url);
        
        sb.append(".");
        return sb.toString();
    }
    
    private String generateIEEE(String author, String title, String publisher, String year, String url) {
        StringBuilder sb = new StringBuilder();
        
        // Initials first format (M. L. Santos)
        String initialsFirst = getInitialsFirst(author);
        
        // Author with initials first
        sb.append(initialsFirst).append(", ");
        
        // Title (will be italicized in frontend)
        sb.append(title).append(". ");
        
        // Publisher
        if (!publisher.isEmpty()) sb.append(publisher).append(", ");
        
        // Year
        if (!year.isEmpty()) sb.append(year).append(". ");
        
        // URL with [Online]. Available: prefix
        if (!url.isEmpty()) {
            sb.append("[Online]. Available: ").append(url);
        }
        
        return sb.toString();
    }
    
    private String getInitialsFirst(String author) {
        String[] parts = author.trim().split("\\s+");
        if (parts.length < 2) {
            return author;
        }
        
        // Last part is last name
        String lastName = parts[parts.length - 1];
        
        // Build initials from first/middle names
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            
            // Skip empty parts
            if (part.isEmpty()) continue;
            
            // Add space before each initial except the first
            if (initials.length() > 0) {
                initials.append(" ");
            }
            
            // If the part already looks like an initial (1-2 chars, possibly with period)
            if (part.length() <= 2 && part.matches("[A-Z]\\.?")) {
                // Already an initial, just ensure it has a period
                if (part.endsWith(".")) {
                    initials.append(part);
                } else {
                    initials.append(part).append(".");
                }
            } else {
                // Full name, extract first letter
                initials.append(part.charAt(0)).append(".");
            }
        }
        
        return initials.toString() + " " + lastName;
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