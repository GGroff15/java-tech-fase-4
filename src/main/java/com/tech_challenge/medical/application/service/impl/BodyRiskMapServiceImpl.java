package com.tech_challenge.medical.application.service.impl;

import com.tech_challenge.medical.application.service.BodyRiskMapService;
import com.tech_challenge.medical.domain.*;
import org.opencv.core.Mat;


import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class BodyRiskMapServiceImpl implements BodyRiskMapService {

    private final BruiseColorAnalyzer colorAnalyzer = new BruiseColorAnalyzer();
    private final ProtectivePostureAnalyzer postureAnalyzer = new ProtectivePostureAnalyzer();
    private final MovementRestrictionAnalyzer movementAnalyzer = new MovementRestrictionAnalyzer();

    @Override
    public Map<BodyRegion, BodyRegionRisk> analyze(Map<BodyRegion, Mat> bodyRegions, List<PoseFrame> poseHistory) {
        Map<BodyRegion, BodyRegionRisk> result = new EnumMap<>(BodyRegion.class);

        for (BodyRegion region : bodyRegions.keySet()) {
            BodyRegionRisk risk = new BodyRegionRisk(region);

            Mat image = bodyRegions.get(region);

            if (colorAnalyzer.hasSuspiciousColor(image)) {
                risk.addIndicator("suspicious_skin_color", 0.35);
            }

            if (movementAnalyzer.isRestricted(region, poseHistory)) {
                risk.addIndicator("limited_movement", 0.30);
            }

            var protectivePoint = postureAnalyzer.protectivePoint(region, poseHistory);
            if (Objects.nonNull(protectivePoint)) {
                risk.addIndicator("protective_posture", 0.35);
            }

            result.put(region, risk);
        }

        return result;

    }
}
