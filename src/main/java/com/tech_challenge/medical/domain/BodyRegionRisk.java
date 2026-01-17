package com.tech_challenge.medical.domain;

import java.util.ArrayList;
import java.util.List;

public class BodyRegionRisk {

    private final BodyRegion region;
    private double riskScore;
    private final List<String> indicators;

    public BodyRegionRisk(BodyRegion region) {
        this.region = region;
        this.indicators = new ArrayList<>();
    }

    public void addIndicator(String indicator, double weight) {
        indicators.add(indicator);
        riskScore += weight;
    }

    public double getRiskScore() {
        return Math.min(riskScore, 1.0);
    }
}
