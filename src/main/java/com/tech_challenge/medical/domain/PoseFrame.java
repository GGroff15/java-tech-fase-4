package com.tech_challenge.medical.domain;



import org.opencv.core.Point;

import java.util.Map;

public class PoseFrame {

    private final Map<String, Point> keypoints;

    public PoseFrame(Map<String, Point> keypoints) {
        this.keypoints = keypoints;
    }

    public Point get(String name) {
        return keypoints.get(name);
    }

    public Map<String, Point> getAll() {
        return keypoints;
    }
}
