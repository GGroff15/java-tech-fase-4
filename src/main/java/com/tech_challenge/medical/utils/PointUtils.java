package com.tech_challenge.medical.utils;

import com.tech_challenge.medical.domain.PoseFrame;
import org.opencv.core.Point;

import java.util.List;


public class PointUtils {

    public static double distance(Point a, Point b) {
        if (a == null || b == null) return Double.MAX_VALUE;

        double dx = a.x - b.x;
        double dy = a.y - b.y;

        return Math.sqrt(dx * dx + dy * dy);
    }

}
