package org.firstinspires.ftc.teamcode;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utils.Pole;
import org.firstinspires.ftc.teamcode.utils.PoleDetectionPipeline;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CameraArray
{
    static final double cameraGapDistance = 69;

    OpenCvCamera RightCamera, LeftCamera;

    PoleDetectionPipeline rightCameraPipeline, leftCameraPipeline;

    public CameraArray(WebcamName RightCamera, WebcamName LeftCamera, int ViewID)
    {
        int[] viewContainerIDs = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(ViewID, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);

        rightCameraPipeline = new PoleDetectionPipeline();
        leftCameraPipeline = new PoleDetectionPipeline();

        this.RightCamera = OpenCvCameraFactory.getInstance().createWebcam(RightCamera, viewContainerIDs[0]);
        this.LeftCamera = OpenCvCameraFactory.getInstance().createWebcam(LeftCamera, viewContainerIDs[1]);
    }

    public void StartStreaming(PoleDetectionPipeline.ViewportStage viewportStage)
    {
        rightCameraPipeline.setViewportStage(viewportStage);
        leftCameraPipeline.setViewportStage(viewportStage);

        OpenCvCamera.AsyncCameraOpenListener openListener = new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                RightCamera.setPipeline(rightCameraPipeline);
                LeftCamera.setPipeline(leftCameraPipeline);

                RightCamera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);
                RightCamera.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

                RightCamera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                LeftCamera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);

                RightCamera.startStreaming(1280, 720, OpenCvCameraRotation.SIDEWAYS_RIGHT);
                LeftCamera.startStreaming(1280, 720, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        };


        RightCamera.openCameraDeviceAsync(openListener);
        LeftCamera.openCameraDeviceAsync(openListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double DistanceToClosestPole()
    {
        ArrayList<Pole> rightCameraView = rightCameraPipeline.getPoles();
        ArrayList<Pole> leftCameraView = leftCameraPipeline.getPoles();

        if (rightCameraView == null || leftCameraView == null) return -1;

        Collections.sort(rightCameraView, Comparator.comparing(Pole::getWidth).reversed());
        Collections.sort(leftCameraView, Comparator.comparing(Pole::getWidth).reversed());

        Pole closestRight = rightCameraView.get(0);
        Pole closestLeft = rightCameraView.get(0);

        StereoSGBM depthMap = StereoSGBM.create(0);

        depthMap.compute(leftCameraPipeline.lastCapturedFrame, rightCameraPipeline.lastCapturedFrame, new Mat());

        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double AngleToClosestPole()
    {
        ArrayList<Pole> rightCameraView = rightCameraPipeline.getPoles();
        ArrayList<Pole> leftCameraView = leftCameraPipeline.getPoles();

        if (rightCameraView == null || leftCameraView == null) return -1;

        Collections.sort(rightCameraView, Comparator.comparing(Pole::getWidth));
        Collections.sort(leftCameraView, Comparator.comparing(Pole::getWidth));

        Pole closestRight = rightCameraView.get(0);
        Pole closestLeft = rightCameraView.get(0);

        return 1;
    }
}

