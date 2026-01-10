package com.tech_challenge.medical.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long analysisCaseId;
    private String summary;
    private String findings;
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;
    @CreatedDate
    private LocalDateTime createdDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Long getAnalysisCaseId() {
        return analysisCaseId;
    }

    public void setAnalysisCaseId(Long analysisCaseId) {
        this.analysisCaseId = analysisCaseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
