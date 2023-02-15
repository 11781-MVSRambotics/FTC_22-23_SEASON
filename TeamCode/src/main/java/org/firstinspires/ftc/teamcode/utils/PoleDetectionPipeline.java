package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
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

    RotatedRect lastRect = new RotatedRect();

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
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.GaussianBlur(yellowFiltered, edges, new Size(15, 15), 0);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) + 1, (2*2) + 1));
        for (int i = 0; i <= 5; i++) {Imgproc.dilate(yellowFiltered, yellowFiltered, kernel);}
        for (int i = 0; i <= 5; i++) {Imgproc.erode(yellowFiltered, yellowFiltered, kernel);}
        Imgproc.Canny(yellowFiltered, edges, 10, 100);

        Imgproc.dilate(edges, edges, kernel);

        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);


        double maxVal = 0;
        int maxValIdx = 0;
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++)
        {
            double contourArea = Imgproc.contourArea(contours.get(contourIdx));
            if (maxVal < contourArea && contourIdx != 0)
            {
                maxVal = contourArea;
                maxValIdx = contourIdx;
            }
        }

        if (maxValIdx != 0)
        {
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(maxValIdx).toArray());
            RotatedRect rectangle = Imgproc.minAreaRect(contour2f);
            Point[] rectPoints = new Point[4];
            rectangle.points(rectPoints);
            for (int i = 0; i < 4; i++) {
                Imgproc.line(input, rectPoints[i], rectPoints[(i + 1) % 4], new Scalar(4, 157, 77), 10);
            }
            lastRect = rectangle;
        }
        else
        {
            Point[] rectPoints = new Point[4];
            lastRect.points(rectPoints);
            for (int i = 0; i < 4; i++) {
                Imgproc.line(input, rectPoints[i], rectPoints[(i + 1) % 4], new Scalar(4, 157, 77), 10);
            }
        }

        // This frame will be returned to the camera preview
        return input;
    }

}
