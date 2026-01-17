package com.tech_challenge.medical.application.service;

import com.tech_challenge.medical.domain.BodyRegion;
import com.tech_challenge.medical.domain.BodyRegionRisk;
import com.tech_challenge.medical.domain.PoseFrame;
import org.opencv.core.Mat;


import java.util.List;
import java.util.Map;

public interface BodyRiskMapService {

    Map<BodyRegion, BodyRegionRisk> analyze(
            Map<BodyRegion, Mat> bodyRegions,
            List<PoseFrame> poseHistory);
}
