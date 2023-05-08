package org.firstinspires.ftc.teamcode.utils;

import android.app.Activity;
import android.view.View;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class CameraCalibrationData
{
    Size boardSize = new Size(8,8);

    public static void main(String[] args)
    {
        Mat frame = new Mat();
        Mat drawToFrame = new Mat();

        Mat distanceCoefficients;
        ArrayList<Mat> savedImages;

        ArrayList<MatOfPoint2f> markerCorners, rejectedCandidates;

        VideoCapture vid = new VideoCapture(0);

        if(!vid.isOpened())
        {
            return;
        }

        int fps = 20;

        while (true)
        {
            if(!vid.read(frame))
            {
                MatOfPoint2f foundPoints = new MatOfPoint2f();

                boolean found = Calib3d.findChessboardCorners(frame, new Size(8, 8), foundPoints,Calib3d.CALIB_CB_ADAPTIVE_THRESH | Calib3d.CALIB_CB_NORMALIZE_IMAGE);
                frame.copyTo(drawToFrame);
                if(found)
                {
                }
            }
        }
    }

    void createChessboardPoints(double cellSideLength, MatOfPoint3f corners)
    {
        for(int x=0; x<boardSize.width; ++x)
        {
            for(int y=0;  y<boardSize.height; ++y)
            {
                corners.push_back(new MatOfPoint3f(new Point3(x * cellSideLength, y * cellSideLength, 0)));
            }
        }
    }

    void getChessboardCorners(ArrayList<MatOfPoint2f> foundCorners)
    {
        final File imageFolder = new File("CalibImages");

        for (File file : Objects.requireNonNull(imageFolder.listFiles()))
        {
            Mat image = Imgcodecs.imread(file.getPath());
            Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);

            MatOfPoint2f imageCorners = new MatOfPoint2f();
            boolean found = Calib3d.findChessboardCorners(image, boardSize, imageCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH | Calib3d.CALIB_CB_NORMALIZE_IMAGE);
            if (found)
            {
                foundCorners.add(imageCorners);
            }
        }
    }
}
