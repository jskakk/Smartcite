package com.appdevg6.smartcite.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GrammarCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String inputText;

    @Column(columnDefinition = "TEXT")
    private String highlightedPhrases;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "grammarCheck", cascade = CascadeType.ALL)
    private List<Annotation> annotations;

    public Long getCheckId() { return checkId; }
    public void setCheckId(Long checkId) { this.checkId = checkId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getInputText() { return inputText; }
    public void setInputText(String inputText) { this.inputText = inputText; }

    public String getHighlightedPhrases() { return highlightedPhrases; }
    public void setHighlightedPhrases(String highlightedPhrases) { this.highlightedPhrases = highlightedPhrases; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Annotation> getAnnotations() { return annotations; }
    public void setAnnotations(List<Annotation> annotations) { this.annotations = annotations; }
}
