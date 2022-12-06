package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

// This class serves as the order of operations for image processing
// It is required by the camera to search and identify poles in frame
// This class scares me
public class PoleDetectionPipeline extends OpenCvPipeline {

    // Process frame runs every time the camera captures a frame (I think)
    // Basically all image manipulation will happen in here (I guess)
    // This method also scares me (I suppose)
    @Override
    public Mat processFrame(Mat input) {

        // Define the range for color detection
        Scalar yellowLower = new Scalar(95, 10, 20);
        Scalar yellowUpper = new Scalar(105, 245, 235);

        // Allocate memory for different varients of the images
        Mat hsv;
        Mat colorMask;
        Mat yellowFiltered;

        // Convert the original image into the hsv color scale
        // Theoretically makes it easier to find the same color in different lighting conditions
        hsv = input;
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);

        // Take the hsv image and mask out anything not in the color range
        colorMask = hsv;
        Core.inRange(hsv, yellowLower, yellowUpper, colorMask);

        // Apply mask to the original image (Maybe)
        yellowFiltered = new Mat();
        Core.bitwise_and(hsv, colorMask, yellowFiltered);

        // This frame will be returned to the camera preview
        return yellowFiltered;
    }
}
