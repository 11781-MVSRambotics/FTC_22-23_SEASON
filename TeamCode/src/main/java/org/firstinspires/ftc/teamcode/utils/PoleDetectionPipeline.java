package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;
import java.util.List;

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
        Scalar yellowLower = new Scalar(95, 165, 15);
        Scalar yellowUpper = new Scalar(105, 250, 255);

        // Allocate memory for different variants of the images
        Mat hsv;
        Mat colorMask;
        Mat yellowFiltered;

        // Convert the original image into the hsv color scale
        // Theoretically makes it easier to find the same color in different lighting conditions
        hsv = input.clone();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_BGR2HSV);

        // Take the hsv image and mask out anything not in the color range
        colorMask = hsv;
        Core.inRange(hsv, yellowLower, yellowUpper, colorMask);

        // Apply mask to the original image (Maybe)
        yellowFiltered = new Mat();
        Core.bitwise_and(hsv, colorMask, yellowFiltered);

        Mat edges = new Mat();
        Imgproc.Canny(yellowFiltered, edges, 75, 255);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) + 1, (2*2) + 1));
        for (int i = 0; i <= 10; i++)
        {
            /*
            if (i % 2 == 0)
            {
                Imgproc.dilate(edges, edges, kernel);
            }
            else
            {
                Imgproc.erode(edges, edges, kernel);
            }
             */
            Imgproc.dilate(edges, edges, kernel);
        }

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat drawing = Mat.zeros(edges.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(0, 255, 0);
            Imgproc.drawContours(drawing, contours, i, color, 2, Imgproc.LINE_8, hierarchy, 0, new Point());
        }

        Mat output = Mat.zeros(input.size(), CvType.CV_8UC3);

        Core.add(input, drawing, output);

        // This frame will be returned to the camera preview
        return drawing;
    }
}
