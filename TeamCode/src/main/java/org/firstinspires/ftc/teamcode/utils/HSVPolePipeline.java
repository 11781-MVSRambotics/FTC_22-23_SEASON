package org.firstinspires.ftc.teamcode.utils;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

import org.checkerframework.checker.units.qual.A;
import org.opencv.core.Core;
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

public class HSVPolePipeline extends OpenCvPipeline {

    public enum ViewportStage
    {
        COLORSPACE,
        COLORMASK,
        YELLOWFILTERED,
        CONTOURS,
        POLES
    }

    public ArrayList<MatOfPoint> contours = new ArrayList<>();
    public ArrayList<RotatedRect> poles = new ArrayList<>();

    public Mat lastCapturedFrame;
    
    public Mat hsvMat = new Mat();
    public Mat colorMaskMat = new Mat();
    public Mat yellowFilteredMat = new Mat();

    public Mat drawingMat_Contours = new Mat();
    public Mat drawingMat_Rectangles = new Mat();

    ViewportStage viewportStage;

    public void setViewportStage(ViewportStage viewportStage)
    {
        this.viewportStage = viewportStage;
    }

    public ArrayList<MatOfPoint> getContours()
    {
        return contours;
    }

    public ArrayList<RotatedRect> getPoles()
    {
        return poles;
    }

    @Override
    public Mat processFrame(Mat inputMat) {

        // Define the range for color detection
        Scalar yellowLower = new Scalar(95, 165, 15);
        Scalar yellowUpper = new Scalar(105, 250, 255);

        // Convert the original image into the hsv color scale
        // Theoretically makes it easier to find the same color in different lighting conditions
        hsvMat = inputMat.clone();
        Imgproc.cvtColor(inputMat, hsvMat, Imgproc.COLOR_BGR2HSV);

        // Take the hsv image and mask out anything not in the color range
        colorMaskMat = hsvMat;
        Core.inRange(hsvMat, yellowLower, yellowUpper, colorMaskMat);

        Core.bitwise_and(hsvMat, colorMaskMat, yellowFilteredMat);

        Imgproc.GaussianBlur(yellowFilteredMat, yellowFilteredMat, new Size(15, 15), 0);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) + 1, (2*2) + 1));
        for (int i = 0; i <= 5; i++) {Imgproc.dilate(yellowFilteredMat, yellowFilteredMat, kernel);}
        for (int i = 0; i <= 5; i++) {Imgproc.erode(yellowFilteredMat, yellowFilteredMat, kernel);}
        Imgproc.Canny(yellowFilteredMat, yellowFilteredMat, 10, 100);

        Imgproc.dilate(yellowFilteredMat, yellowFilteredMat, kernel);

        Imgproc.findContours(yellowFilteredMat, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        inputMat.copyTo(drawingMat_Contours);
        inputMat.copyTo(drawingMat_Rectangles);

        Imgproc.drawContours(drawingMat_Contours, contours, -1, new Scalar(255, 255, 255), 3, 4);

        for (MatOfPoint contour : contours)
        {
            RotatedRect pole = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

            if (pole.size.area() > Pole.minArea)
            {
                Point[] vertices = new Point[4];
                pole.points(vertices);

                Imgproc.putText(drawingMat_Rectangles, "H/W Ratio: " + pole.size.height/pole.size.width, vertices[0], 1, 10, new Scalar(255, 255, 255));

                for (int i = 0; i < 4; i++)
                {
                    Imgproc.line(drawingMat_Rectangles, vertices[i], vertices[(i + 1) % 4], new Scalar(255, 255, 255), 4);
                }
                poles.add(pole);
            }
        }

        switch (viewportStage)
        {
            case COLORSPACE:
                return hsvMat;
            case COLORMASK:
                return colorMaskMat;
            case YELLOWFILTERED:
                return yellowFilteredMat;
            case CONTOURS:
                return drawingMat_Contours;
            case POLES:
                return drawingMat_Rectangles;
            default:
                return inputMat;
        }
    }

}
