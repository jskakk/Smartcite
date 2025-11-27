package com.appdevg6.smartcite.entity;
 
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
 
@Entity
@Table(name = "citation_styles")
public class CitationStyle {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_id")
    private Long styleId;
 
    @Column(name = "style_name", nullable = false)
    private String styleName;
 
    @Column(name = "description")
    private String description;
 
    @OneToMany(mappedBy = "style", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Citation> citations = new ArrayList<>();
 
    public CitationStyle() {}
 
    public CitationStyle(String styleName, String description) {
        this.styleName = styleName;
        this.description = description;
    }
 
    public Long getStyleId() { return styleId; }
    public void setStyleId(Long styleId) { this.styleId = styleId; }
 
    public String getStyleName() { return styleName; }
    public void setStyleName(String styleName) { this.styleName = styleName; }
 
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
 
    public List<Citation> getCitations() { return citations; }
    public void setCitations(List<Citation> citations) { this.citations = citations; }
 
    public void addCitation(Citation citation) {
        citations.add(citation);
        citation.setStyle(this);
    }
 
}