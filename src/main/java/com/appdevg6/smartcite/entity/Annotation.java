package com.appdevg6.smartcite.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long annotationId;

    @ManyToOne
    @JoinColumn(name = "check_id", nullable = false)
    private GrammarCheck grammarCheck;

    private String errorType;
    
    @Column(columnDefinition = "TEXT")
    private String errorText;
    
    @Column(columnDefinition = "TEXT")
    private String suggestion;

    private String severity;
    private Integer positionIndex;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getAnnotationId() { return annotationId; }
    public void setAnnotationId(Long annotationId) { this.annotationId = annotationId; }

    public GrammarCheck getGrammarCheck() { return grammarCheck; }
    public void setGrammarCheck(GrammarCheck grammarCheck) { this.grammarCheck = grammarCheck; }

    public String getErrorType() { return errorType; }
    public void setErrorType(String errorType) { this.errorType = errorType; }

    public String getErrorText() { return errorText; }
    public void setErrorText(String errorText) { this.errorText = errorText; }

    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public Integer getPositionIndex() { return positionIndex; }
    public void setPositionIndex(Integer positionIndex) { this.positionIndex = positionIndex; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
