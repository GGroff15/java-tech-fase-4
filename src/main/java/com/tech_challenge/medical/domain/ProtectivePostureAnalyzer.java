package com.tech_challenge.medical.domain;

import com.tech_challenge.medical.utils.PointUtils;
import org.opencv.core.Point;

import java.util.List;

public class ProtectivePostureAnalyzer {

    public Point protectivePoint(BodyRegion region, List<PoseFrame> history) {

        if (history == null || history.isEmpty()) return null;

        PoseFrame last = history.getLast();

        if (region == BodyRegion.ARM_LEFT) {
            return midpoint(last.get("left_wrist"), last.get("left_elbow"));
        }

        if (region == BodyRegion.ARM_RIGHT) {
            return midpoint(last.get("right_wrist"), last.get("right_elbow"));
        }

        return null;
    }

    private boolean close(Point a, Point b) {
        return a != null && b != null && PointUtils.distance(a, b) > 0.07;
    }

    private Point midpoint(Point a, Point b) {
        if (a == null || b == null) return null;

        double dist = PointUtils.distance(a, b);

        // threshold de "proteção"
        if (dist > 0.07) return null;

        return new Point(
                (a.x + b.x) / 2,
                (a.y + b.y) / 2
        );
    }
}
