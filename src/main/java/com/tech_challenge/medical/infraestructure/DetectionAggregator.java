package com.tech_challenge.medical.infraestructure;

import com.tech_challenge.medical.domain.Detection;
import com.tech_challenge.medical.domain.FrameAnalysisResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DetectionAggregator {

    public List<Detection> aggregate(List<FrameAnalysisResult> frames) {

        List<Detection> detections = new ArrayList<>();

        for (FrameAnalysisResult frame : frames) {

            if (frame.signals().getOrDefault("protective_posture", 0.0) > 0.6) {
                detections.add(new Detection(
                        "protective_posture",
                        frame.signals().get("protective_posture"),
                        frame.timestamp()
                ));
            }

            if (frame.signals().getOrDefault("facial_pain", 0.0) > 0.7) {
                detections.add(new Detection(
                        "facial_pain",
                        frame.signals().get("facial_pain"),
                        frame.timestamp()
                ));
            }
        }

        return detections;
    }
}
