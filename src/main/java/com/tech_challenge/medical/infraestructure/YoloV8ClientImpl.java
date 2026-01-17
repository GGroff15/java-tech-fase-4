package com.tech_challenge.medical.infraestructure;

import com.tech_challenge.medical.application.service.BodyRiskMapService;
import com.tech_challenge.medical.infraestructure.dto.ProcessVideoRequest;
import com.tech_challenge.medical.infraestructure.dto.ProcessVideoResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class YoloV8ClientImpl implements YoloV8Client {

    @Autowired
    private BodyRiskMapService bodyRiskMapService;

    @Override
    public ProcessVideoResponse analyzeFrame(ProcessVideoRequest request) {
        return null;
    }
}
