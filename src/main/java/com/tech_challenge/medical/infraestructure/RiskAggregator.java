package com.tech_challenge.medical.infraestructure;

import com.tech_challenge.medical.domain.Detection;
import com.tech_challenge.medical.domain.RiskLevel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RiskAggregator {

    public RiskLevel calculate(List<Detection> detections) {

        long painEvents =
                detections.stream()
                        .filter(d -> d.label().contains("pain"))
                        .count();

        long postureEvents =
                detections.stream()
                        .filter(d -> d.label().contains("protective"))
                        .count();

        if (painEvents >= 3 && postureEvents >= 2) {
            return RiskLevel.HIGH;
        }

        if (painEvents >= 1 || postureEvents >= 1) {
            return RiskLevel.MEDIUM;
        }

        return RiskLevel.LOW;
    }
}
