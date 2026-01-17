package com.tech_challenge.medical.utils;

import com.tech_challenge.medical.domain.PoseFrame;
import org.opencv.core.Point;

import java.util.List;

public final class PoseUtils {

    public static Point averagePoint(PoseFrame pose, List<String> keypoints) {

        double sumX = 0;
        double sumY = 0;
        int count = 0;

        for (String key : keypoints) {
            Point p = pose.get(key);
            if (p != null) {
                sumX += p.x;
                sumY += p.y;
                count++;
            }
        }

        if (count == 0) return null;

        return new Point(sumX / count, sumY / count);
    }
}
