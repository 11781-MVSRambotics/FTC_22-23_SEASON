package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class PoleDetectionPipeline extends OpenCvPipeline {
    @Override
    public Mat processFrame(Mat input) {

        Scalar yellowLower = new Scalar(95, 15, 15);
        Scalar yellowUpper = new Scalar(100, 200, 200);

        Mat hsv = new Mat();
        Mat colorMask = new Mat();
        Mat yellowFiltered = new Mat();

        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsv, yellowLower, yellowUpper, colorMask);
        Core.bitwise_and(hsv, colorMask, yellowFiltered);

        return yellowFiltered;
    }
}
