package com.tech_challenge.medical.application;

import com.tech_challenge.medical.domain.AnalysisCase;
import com.tech_challenge.medical.domain.AnalysisResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorService.class);

    private final ReportProcessorStrategy reportProcessor;

    public ProcessorService(ReportProcessorStrategy reportProcessor) {
        this.reportProcessor = reportProcessor;
    }

    public AnalysisResult process(AnalysisCase analysisCase) {
        LOGGER.info("Processing analysis case {}, type {}", analysisCase.getId(), analysisCase.getType());

        return reportProcessor.process(analysisCase);
    }

}
