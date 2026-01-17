package com.tech_challenge.medical.application.processor;

import com.tech_challenge.medical.domain.*;
import com.tech_challenge.medical.infraestructure.DetectionAggregator;
import com.tech_challenge.medical.infraestructure.RiskAggregator;
import com.tech_challenge.medical.infraestructure.VideoAnalyzer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
public class VideoProcessorStrategy implements ProcessorStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoProcessorStrategy.class);

    private final VideoAnalyzer videoAnalyzer;

    private final DetectionAggregator detectionAggregator;

    private final RiskAggregator  riskAggregator;

    public VideoProcessorStrategy(VideoAnalyzer videoAnalyzer, DetectionAggregator detectionAggregator, RiskAggregator riskAggregator) {
        this.videoAnalyzer = videoAnalyzer;
        this.detectionAggregator = detectionAggregator;
        this.riskAggregator = riskAggregator;
    }

    @Override
    public AnalysisResult process(AnalysisCase analysisCase) {

            LOGGER.info("Processing Video Processor Strategy");

            File video = new File(analysisCase.getRawFilePath());

            List<FrameAnalysisResult> frames = videoAnalyzer.analyze(video);

            List<Detection> detections = detectionAggregator.aggregate(frames);

            RiskLevel riskLevel = riskAggregator.calculate(detections);

        return populateAnalysisResult(analysisCase, riskLevel, detections);
    }

    @NotNull
    private static AnalysisResult populateAnalysisResult(AnalysisCase analysisCase, RiskLevel riskLevel, List<Detection> detections) {
        var analysisResult = new AnalysisResult();

        analysisResult.setRiskLevel(riskLevel);
        analysisResult.setAnalysisCaseId(analysisCase.getId());
        analysisResult.setCreatedDate(LocalDateTime.now());
        analysisResult.setDetections(detections);

        return analysisResult;
    }
}
