package org.firstinspires.ftc.teamcode.utils;

import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;

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


public class SummitsPipeline extends OpenCvPipeline {

    public Mat lastCapturedFrame;

    HSVPolePipeline.ViewportStage viewportStage;

    ArrayList<MatOfPoint> contours = new ArrayList<>();
    ArrayList<RotatedRect> poles = new ArrayList<>();

    Mat yCrCbMat = new Mat();

    Mat CrMat = new Mat();
    Mat CrThreshMat = new Mat();
    Mat CrThreshMat_Inv = new Mat();
    Mat CrThreshMat_Combined = new Mat();

    Mat CbMat = new Mat();
    Mat CbThreshMat = new Mat();
    Mat CbThreshMat_Inv = new Mat();
    Mat CbThreshMat_Combined = new Mat();

    Mat CrCb_Combined = new Mat();

    Mat drawingMat_Contours = new Mat();
    Mat drawingMat_Rectangles = new Mat();

    @Override
    public Mat processFrame(Mat input)
    {
        lastCapturedFrame = input;

        // Define the range for color detection
        contours.clear();
        poles.clear();

        Imgproc.cvtColor(input, yCrCbMat, Imgproc.COLOR_RGB2YCrCb);

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

        input.copyTo(drawingMat_Contours);
        input.copyTo(drawingMat_Rectangles);

        Imgproc.drawContours(drawingMat_Contours, contours, -1, new Scalar(255, 255, 255), 3, 4);

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

        return drawingMat_Rectangles;
    }
}
