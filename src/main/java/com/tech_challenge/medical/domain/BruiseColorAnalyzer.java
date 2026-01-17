package com.tech_challenge.medical.domain;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class BruiseColorAnalyzer {

    public boolean hasSuspiciousColor(Mat region) {

        Scalar mean = Core.mean(region);

        double blue = mean.val[0];
        double green = mean.val[1];
        double red = mean.val[2];

        // tons arroxeados / azulados / escuros
        return blue > red && blue > green && blue > 110;
    }

}
