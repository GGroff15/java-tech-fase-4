package com.tech_challenge.medical.application;

import com.tech_challenge.medical.domain.*;
import com.tech_challenge.medical.domain.event.AnalysisCaseCreatedEvent;
import com.tech_challenge.medical.infraestructure.AnalysisCaseRepository;
import com.tech_challenge.medical.infraestructure.AnalysisResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
class ProcessReportListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessReportListener.class);

    private final AnalysisCaseRepository analysisCaseRepository;
    private final AnalysisResultRepository analysisResultRepository;
    private final TextExtractorStrategyResolver textExtractor;

    ProcessReportListener(AnalysisCaseRepository analysisCaseRepository, AnalysisResultRepository analysisResultRepository, TextExtractorStrategyResolver textExtractor) {
        this.analysisCaseRepository = analysisCaseRepository;
        this.analysisResultRepository = analysisResultRepository;
        this.textExtractor = textExtractor;
    }

    @Async
    @EventListener
    void on(AnalysisCaseCreatedEvent event) {
        LOGGER.info("Processing report for AnalysisCase ID: {}", event.getAnalysisCaseId());

        analysisCaseRepository.findById(event.getAnalysisCaseId()).ifPresent(this::processReport);

        LOGGER.info("Processed report for AnalysisCase ID: {}", event.getAnalysisCaseId());
    }

    private void processReport(AnalysisCase analysisCase) {
        analysisCase.setStatus(Status.PROCESSING);
        analysisCaseRepository.save(analysisCase);

        String rawFilePath = analysisCase.getRawFilePath();

        try {
            String fileText = textExtractor.extract(rawFilePath);

            // Send text to AI model for analysis
            System.out.println(fileText);

            AnalysisResult analysisResult = new AnalysisResult();
            analysisResult.setRiskLevel(RiskLevel.MEDIUM);
            analysisResult.setFindings("");
            analysisResult.setSummary("");
            analysisResult.setAnalysisCaseId(analysisCase.getId());

            analysisResultRepository.save(analysisResult);

            analysisCase.setStatus(Status.COMPLETED);
        } catch (Exception e) {
            LOGGER.error("Error processing report for AnalysisCase ID: {}: {}", analysisCase.getId(), e.getMessage());
            analysisCase.setStatus(Status.FAILED);
        } finally {
            analysisCase.setProcessedAt(Instant.now());
            analysisCaseRepository.save(analysisCase);
        }
    }
}
