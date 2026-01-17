package com.tech_challenge.medical.domain;

import com.tech_challenge.medical.utils.PointUtils;
import com.tech_challenge.medical.utils.PoseUtils;
import org.opencv.core.Point;

import java.util.List;

public class MovementRestrictionAnalyzer {

    public boolean isRestricted(BodyRegion region, List<PoseFrame> frames) {

        if (frames.size() < 10) return false;

        double totalMovement = 0;

        for (int i = 1; i < frames.size(); i++) {
            Point prev = PoseUtils.averagePoint(
                    frames.get(i - 1),
                    BodyRegionKeypoints.getKeypoints(region)
            );

            Point curr = PoseUtils.averagePoint(
                    frames.get(i),
                    BodyRegionKeypoints.getKeypoints(region)
            );

            if (prev != null && curr != null) {
                totalMovement += PointUtils.distance(prev, curr);
            }
        }

        return totalMovement < 0.04;
    }
}
