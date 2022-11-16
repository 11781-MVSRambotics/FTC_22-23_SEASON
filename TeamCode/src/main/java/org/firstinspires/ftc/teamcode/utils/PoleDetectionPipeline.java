package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class PoleDetectionPipeline extends OpenCvPipeline {
    @Override
    public Mat processFrame(Mat input) {

        Scalar yellowLower = new Scalar(95, 10, 20);
        Scalar yellowUpper = new Scalar(105, 245, 235);

        Mat hsv;
        Mat colorMask;
        Mat yellowFiltered;

        hsv = input;
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);
        colorMask = hsv;
        Core.inRange(hsv, yellowLower, yellowUpper, colorMask);
        yellowFiltered = new Mat();
        Core.bitwise_and(hsv, colorMask, yellowFiltered);

        return yellowFiltered;
    }
}
