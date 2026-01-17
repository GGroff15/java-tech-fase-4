package com.tech_challenge.medical.domain;

import java.util.List;
import java.util.Map;

public final class BodyRegionKeypoints {


    private static final Map<BodyRegion, List<String>> MAP = Map.of(
            BodyRegion.ARM_LEFT, List.of("left_shoulder", "left_elbow", "left_wrist"),
            BodyRegion.ARM_RIGHT, List.of("right_shoulder", "right_elbow", "right_wrist"),
            BodyRegion.LEG_LEFT, List.of("left_hip", "left_knee", "left_ankle"),
            BodyRegion.LEG_RIGHT, List.of("right_hip", "right_knee", "right_ankle"),
            BodyRegion.TORSO, List.of("left_shoulder", "right_shoulder", "left_hip", "right_hip"),
            BodyRegion.FACE, List.of("nose", "left_eye", "right_eye", "mouth_center")
    );

    private BodyRegionKeypoints() {}

    public static List<String> getKeypoints(BodyRegion region) {
        return MAP.getOrDefault(region, List.of());
    }
}
