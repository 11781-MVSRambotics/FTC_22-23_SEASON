package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class HSVPolePipeline extends OpenCvPipeline {

    static final Scalar YELLOW_UPPER_HUE = new Scalar(107);
    static final Scalar YELLOW_LOWER_HUE = new Scalar(95);

    static final Scalar YELLOW_UPPER_SAT = new Scalar(200);
    static final Scalar YELLOW_LOWER_SAT = new Scalar(100);

    public enum ViewportStage
    {
        HSV,
        HUE,
        HUE_THRESH,
        SATURATION,
        SAT_THRESH,
        COMBINED_THRESH,
        POLES
    }

    ArrayList<RotatedRect> poles = new ArrayList<>();
    ArrayList<MatOfPoint> contours = new ArrayList<>();

    Mat lastCapturedFrame = new Mat();
    Mat lastProcessedFrame = new Mat();
    
    Mat hsvMat = new Mat();
    Mat hueMat = new Mat();
    Mat saturationMat = new Mat();
    Mat hueThreshMat = new Mat();
    Mat saturationThreshMat = new Mat();
    Mat combinedThreshMat = new Mat();
    Mat drawingMat_Rectangles = new Mat();

    ViewportStage viewportStage;

    public void setViewportStage(ViewportStage viewportStage)
    {
        this.viewportStage = viewportStage;
    }

    public Mat getLastCapturedFrame()
    {
        return lastCapturedFrame;
    }

    public Mat getLastProcessedFrame()
    {
        return lastProcessedFrame;
    }

    public ArrayList<RotatedRect> getPoles()
    {
        return poles;
    }

    static void drawPole(Mat img, RotatedRect pole)
    {
        Point[] vertices = new Point[4];
        pole.points(vertices);

        //Imgproc.putText(img, pole.getType() + " Pole", vertices[0], 1, 3, new Scalar(255, 0, 0));

        for (int i = 0; i < 4; i++)
        {
            Imgproc.line(img, vertices[i], vertices[(i + 1) % 4], new Scalar(255, 0, 0), 2);
        }
    }

    @Override
    public Mat processFrame(Mat inputMat)
    {
        inputMat.copyTo(drawingMat_Rectangles);

        contours.clear();
        poles.clear();

        // Convert the original image into the hsv color scale
        // Theoretically makes it easier to find the same color in different lighting conditions
        Imgproc.cvtColor(inputMat, hsvMat, Imgproc.COLOR_BGR2HSV);

        Core.extractChannel(hsvMat, hueMat, 0);
        Core.extractChannel(hsvMat, saturationMat, 1);

        Core.inRange(hueMat, YELLOW_LOWER_HUE, YELLOW_UPPER_HUE, hueThreshMat);
        Core.inRange(saturationMat, YELLOW_LOWER_SAT, YELLOW_UPPER_SAT, saturationThreshMat);

        Core.bitwise_and(hueThreshMat, saturationThreshMat, combinedThreshMat);

        Imgproc.findContours(combinedThreshMat, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        for (MatOfPoint contour : contours)
        {
            RotatedRect pole = Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

            if (pole.size.area() > Pole.minArea)
            {
                drawPole(drawingMat_Rectangles, pole);
                poles.add(pole);
            }
        }

        Mat out = new Mat();

        if (viewportStage == null) {return inputMat;}

        switch (viewportStage)
        {
            case HSV:
                hsvMat.copyTo(out);
                break;
            case HUE:
                hueMat.copyTo(out);
                break;
            case SATURATION:
                saturationMat.copyTo(out);
                break;
            case HUE_THRESH:
                hueThreshMat.copyTo(out);
                break;
            case SAT_THRESH:
                saturationThreshMat.copyTo(out);
                break;
            case COMBINED_THRESH:
                combinedThreshMat.copyTo(out);
                break;
            case POLES:
                drawingMat_Rectangles.copyTo(out);
                break;
            default:
                inputMat.copyTo(out);
                break;
        }

        inputMat.copyTo(lastCapturedFrame);

        /*
        inputMat.release();
        hsvMat.release();
        hueMat.release();
        saturationMat.release();
        hueThreshMat.release();
        saturationThreshMat.release();
        combinedThreshMat.release();
        drawingMat_Rectangles.release();
         */

        out.copyTo(lastProcessedFrame);
        return out;

    }

}
