package org.firstinspires.ftc.teamcode.utils;

import org.checkerframework.checker.units.qual.A;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CameraCalibrationData
{
    public static void main(String[] args)
    {
        final File imageFolder = new File("CalibImages");
        Size chessboardSize = new Size(8,8);

        List<Mat> objectPoints = new ArrayList<>();
        List<Mat> imagePoints = new ArrayList<>();

        Mat intrinsic = new Mat(3, 3, CvType.CV_32FC1);

        MatOfPoint3f obj = new MatOfPoint3f();
        for(int x=0; x<chessboardSize.width; ++x)
        {
            for(int y=0;  y<chessboardSize.height; ++y)
            {
                obj.push_back(new MatOfPoint3f(new Point3(x, y, 0)));
            }
        }

        for (File file : Objects.requireNonNull(imageFolder.listFiles()))
        {
            Mat image = Imgcodecs.imread(file.getPath());
            Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);

            MatOfPoint2f imageCorners = new MatOfPoint2f();
            boolean found = Calib3d.findChessboardCorners(image, chessboardSize, imageCorners, Calib3d.CALIB_CB_ADAPTIVE_THRESH + Calib3d.CALIB_CB_NORMALIZE_IMAGE + Calib3d.CALIB_CB_FAST_CHECK);
            if (found)
            {
                objectPoints.add(obj);
                imagePoints.add(imageCorners);
            }
        }

        ArrayList<Mat> rvecs = new ArrayList<>();
        ArrayList<Mat> tvecs = new ArrayList<>();
        Mat distCoeffs = new Mat();
        intrinsic.put(0, 0, 1);
        intrinsic.put(1, 1, 1);

        Calib3d.calibrateCamera(objectPoints, imagePoints, new Size(), intrinsic, distCoeffs, rvecs, tvecs);














        /*
        final int minDisparity = 0;
        final int numDisparities = 16;
        final int blockSize = 3;
        final int P1 = 0;
        final int P2 = 0;
        final int disp12MaxDiff = 0;
        final int preFilterCap = 0;
        final int uniquenessRatio = 0;
        final int speckleWindowSize = 0;
        final int speckleRange = 0;
        final int mode = StereoSGBM.MODE_SGBM;
        StereoSGBM stereoProcessor = StereoSGBM.create(
                minDisparity,
                numDisparities,
                blockSize,
                P1,
                P2,
                disp12MaxDiff,
                preFilterCap,
                uniquenessRatio,
                speckleWindowSize,
                speckleRange,
                mode
        );

        List<Mat> objectPoints = new ArrayList<>();
        List<Mat> imagePoints1 = new ArrayList<>();
        List<Mat> imagePoints2 = new ArrayList<>();
        Mat cameraMatrix1 = new Mat();
        Mat distCoeffs1 = new Mat();
        Mat cameraMatrix2 = new Mat();
        Mat distCoeffs2 = new Mat();
        Size imageSize = new Size();
        Mat R = new Mat();
        Mat T = new Mat();
        Mat E = new Mat();
        Mat F = new Mat();


        Mat distCoeffsLeft = new Mat();
        Mat distCoeffsRight = new Mat();
        Size imageSize = new Size();
        Mat R = new Mat();
        Mat T = new Mat();
        Mat R1 = new Mat();
        Mat R2 = new Mat();
        Mat P1 = new Mat();
        Mat P2 = new Mat();
        Mat Q = new Mat();
        int flags = Calib3d.CALIB_ZERO_DISPARITY;
        double alpha = -1;
        Size newImageSize = new Size(0, 0);
        Calib3d.stereoRectify(
                leftPipeline.getLastCapturedFrame(),
                distCoeffsLeft,
                rightPipeline.getLastCapturedFrame(),
                distCoeffsRight,
                imageSize,
                R,
                T,
                R1,
                R2,
                P1,
                P2,
                Q,
                flags,
                alpha,
                newImageSize
        );

         */
    }
}
