package com.tech_challenge.medical.application;

import com.tech_challenge.medical.domain.AnalysisCaseCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class ProcessReportListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessReportListener.class);

    @Async
    @EventListener
    void on(AnalysisCaseCreatedEvent event) throws InterruptedException {
        LOGGER.info("Processing report for AnalysisCase ID: {}", event.getAnalysisCaseId());

        Thread.sleep(10000); // Simulate processing delay

        LOGGER.info("Processed report for AnalysisCase ID: {}", event.getAnalysisCaseId());
    }

}
