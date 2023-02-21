package org.firstinspires.ftc.teamcode.utils;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class PoleDetectionPipeline extends OpenCvPipeline {

    public enum ViewportStage
    {
        COLORSPACE,
        RED_CHANNEL,
        RED_CHANNEL_THRESH,
        BLUE_CHANNEL,
        BLUE_CHANNEL_THRESH,
        CONTOURS,
        POLES
    }

    public Mat lastCapturedFrame;

    ViewportStage viewportStage;

    ArrayList<MatOfPoint> contours = new ArrayList<>();
    ArrayList<Pole> poles = new ArrayList<>();

    Mat yCrCbMat = new Mat();
    Mat CrMat, CrThreshMat, CrThreshMat_Inv, CrThreshMat_Combined = new Mat();
    Mat CbMat, CbThreshMat, CbThreshMat_Inv, CbThreshMat_Combined = new Mat();
    Mat CrCb_Combined = new Mat();

    Mat drawingMat_Contours = new Mat();
    Mat drawingMat_Rectangles = new Mat();

    public void setViewportStage(ViewportStage viewportStage)
    {
        this.viewportStage = viewportStage;
    }

    public ArrayList<Pole> getPoles()
    {
        return poles;
    }

    public ArrayList<Pole> getPoles(Pole.Type type)
    {
        ArrayList<Pole> out = new ArrayList<>();

        for (Pole pole : poles)
        {
            if (pole.getType() == type)
            {
                out.add(pole);
            }
        }

        return out;
    }

    public static void drawPole(Mat image, Pole pole, Scalar color, int thickness)
    {
        Point[] vertices = new Point[4];
        pole.points(vertices);

        Imgproc.putText(image, "H/W Ratio: " + pole.getType().toString(), vertices[0], 1, 20, new Scalar(255, 255, 255));

        for (int i = 0; i < 4; i++)
        {
            Imgproc.line(image, vertices[i], vertices[(i + 1) % 4], color, thickness);
        }
    }

    @Override
    public Mat processFrame(Mat inputMat) {

        lastCapturedFrame = inputMat;

        // Define the range for color detection
        contours.clear();
        poles.clear();

        Imgproc.cvtColor(inputMat, yCrCbMat, Imgproc.COLOR_RGB2YCrCb);

        Core.extractChannel(yCrCbMat, CrMat, 1);

        Imgproc.threshold(CrMat, CrThreshMat, 145, 255, THRESH_BINARY);
        Imgproc.threshold(CrMat, CrThreshMat_Inv, 167,255, THRESH_BINARY_INV);

        Core.addWeighted(CrThreshMat, 0.5, CrThreshMat_Inv, 0.5, 0, CrThreshMat_Combined);

        Core.extractChannel(yCrCbMat, CbMat, 2);

        Imgproc.threshold(CbMat, CbThreshMat, 83, 255, THRESH_BINARY);
        Imgproc.threshold(CbMat, CbThreshMat_Inv, 90, 255, THRESH_BINARY_INV);

        Core.addWeighted(CbThreshMat, 0.5, CbThreshMat_Inv, 0.5, 0, CbThreshMat_Combined);

        Core.addWeighted(CrThreshMat_Combined, 0.5, CbThreshMat_Combined, 0.5, 0, CrCb_Combined);

        Imgproc.threshold(CrCb_Combined, CrCb_Combined, 130, 255, THRESH_BINARY);

        Imgproc.findContours(CrCb_Combined, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        inputMat.copyTo(drawingMat_Contours);

        Imgproc.drawContours(drawingMat_Contours, contours, -1, new Scalar(255, 255, 255), 3, 4);

        for (MatOfPoint contour : contours)
        {
            Pole pole = (Pole) Imgproc.minAreaRect(new MatOfPoint2f(contour.toArray()));

            if (pole.size.area() < Pole.minArea)
            {
                drawPole(drawingMat_Rectangles, pole, new Scalar(255, 255, 255), 10);
                poles.add(pole);
            }
        }

        switch (viewportStage)
        {
            case COLORSPACE:
                return yCrCbMat;
            case RED_CHANNEL:
                return CrMat;
            case BLUE_CHANNEL:
                return CbMat;
            case RED_CHANNEL_THRESH:
                return CrThreshMat_Combined;
            case BLUE_CHANNEL_THRESH:
                return CbThreshMat_Combined;
            case CONTOURS:
                return drawingMat_Contours;
            case POLES:
                return drawingMat_Rectangles;
            default:
                return inputMat;
        }
    }

}
