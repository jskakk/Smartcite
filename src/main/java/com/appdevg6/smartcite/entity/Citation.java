package com.appdevg6.smartcite.entity;
 
import jakarta.persistence.*;
import java.time.LocalDateTime;
 
@Entity
@Table(name = "citations")
public class Citation {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "citation_id")
    private Long citationId;
 
    @Column(name = "author")
    private String author;
 
    @Column(name = "title")
    private String title;
 
    @Column(name = "year")
    private Integer year;
 
    @Column(name = "publisher")
    private String publisher;
 
    @Column(name = "publication_place")
    private String publicationPlace;
 
    @Column(name = "url")
    private String url;
 
    @Column(name = "generated_citation", columnDefinition = "TEXT")
    private String generatedCitation;
 
    @Column(name = "created_at")
    private LocalDateTime createdAt;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "style_id")
    private CitationStyle style;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_view_id")
    private User user;
 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;
 
    public Citation() {
        this.createdAt = LocalDateTime.now();
    }
 
    // Getters and Setters
    public Long getCitationId() { return citationId; }
    public void setCitationId(Long citationId) { this.citationId = citationId; }
 
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
 
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
 
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
 
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
 
    public String getPublicationPlace() { return publicationPlace; }
    public void setPublicationPlace(String publicationPlace) { this.publicationPlace = publicationPlace; }
 
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
 
    public String getGeneratedCitation() { return generatedCitation; }
    public void setGeneratedCitation(String generatedCitation) { this.generatedCitation = generatedCitation; }
 
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
 
    public CitationStyle getStyle() { return style; }
    public void setStyle(CitationStyle style) { this.style = style; }
 
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
 
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
 
}