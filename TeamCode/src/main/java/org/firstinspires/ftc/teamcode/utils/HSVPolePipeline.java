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
import java.util.Vector;

public class HSVPolePipeline extends OpenCvPipeline {

    public enum ViewportStage
    {
        HSV,
        HUE,
        HUE_THRESH,
        SATURATION,
        SAT_THRESH,
        COMBINED_THRESH,
        VALUE,
        POLES
    }

    public ArrayList<MatOfPoint> contours = new ArrayList<>();
    public ArrayList<RotatedRect> poles = new ArrayList<>();

    Scalar yellowUpperHue = new Scalar(107);
    Scalar yellowLowerHue = new Scalar(95);

    Scalar yellowUpperSat = new Scalar(220);
    Scalar yellowLowerSat = new Scalar(150);

    public Mat lastCapturedFrame;
    
    public Mat hsvMat = new Mat();
    public Mat hueThreshMat = new Mat();
    public Mat saturationThreshMat = new Mat();
    public Mat combinedThreshMat = new Mat();

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
    public Mat processFrame(Mat inputMat)
    {
        contours.clear();
        poles.clear();

        lastCapturedFrame = inputMat;

        // Convert the original image into the hsv color scale
        // Theoretically makes it easier to find the same color in different lighting conditions
        Imgproc.cvtColor(inputMat, hsvMat, Imgproc.COLOR_BGR2HSV);

        Vector<Mat> HSVChannels = new Vector<>();
        Core.split(hsvMat, HSVChannels);

        Core.inRange(HSVChannels.get(0), yellowLowerHue, yellowUpperHue, hueThreshMat);
        Core.inRange(HSVChannels.get(1), yellowLowerSat, yellowUpperSat, saturationThreshMat);

        Core.bitwise_and(hueThreshMat, saturationThreshMat, combinedThreshMat);

        Imgproc.findContours(combinedThreshMat, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        inputMat.copyTo(drawingMat_Rectangles);

        for (MatOfPoint contour : contours)
        {
            RotatedRect pole = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

            if (pole.size.area() > Pole.minArea)
            {
                Point[] vertices = new Point[4];
                pole.points(vertices);

                //Imgproc.putText(drawingMat_Rectangles, "H/W Ratio: " + pole.size.height/pole.size.width, vertices[0], 1, 10, new Scalar(255, 255, 255));

                for (int i = 0; i < 4; i++)
                {
                    Imgproc.line(drawingMat_Rectangles, vertices[i], vertices[(i + 1) % 4], new Scalar(255, 255, 255), 4);
                }
                poles.add(pole);
            }
        }


        switch (viewportStage)
        {
            case HSV:
                return hsvMat;
            case HUE:
                return HSVChannels.get(0);
            case SATURATION:
                return HSVChannels.get(1);
            case VALUE:
                return HSVChannels.get(2);
            case HUE_THRESH:
                return hueThreshMat;
            case SAT_THRESH:
                return saturationThreshMat;
            case COMBINED_THRESH:
                return combinedThreshMat;
            case POLES:
                return drawingMat_Rectangles;
            default:
                return inputMat;
        }
    }

}
