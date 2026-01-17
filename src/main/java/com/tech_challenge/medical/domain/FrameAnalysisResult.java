package com.tech_challenge.medical.domain;

import org.opencv.core.Point;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record FrameAnalysisResult(
        List<Detection> detections,
        Map<String, Double> signals, // posture, face, body
        Map<String, Point> anchors,
        Instant timestamp) {

    public boolean hasAnomalies() {
        return detections != null && !detections.isEmpty();
    }

}
