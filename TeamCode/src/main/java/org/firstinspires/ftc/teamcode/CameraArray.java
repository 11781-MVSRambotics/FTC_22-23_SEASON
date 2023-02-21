package org.firstinspires.ftc.teamcode;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utils.Pole;
import org.firstinspires.ftc.teamcode.utils.HSVPolePipeline;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;
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

    HSVPolePipeline rightCameraPipeline, leftCameraPipeline;

    public CameraArray(WebcamName RightCamera, WebcamName LeftCamera, int ViewID)
    {
        int[] viewContainerIDs = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(ViewID, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY);

        rightCameraPipeline = new HSVPolePipeline();
        leftCameraPipeline = new HSVPolePipeline();

        this.RightCamera = OpenCvCameraFactory.getInstance().createWebcam(RightCamera, viewContainerIDs[0]);
        this.LeftCamera = OpenCvCameraFactory.getInstance().createWebcam(LeftCamera, viewContainerIDs[1]);
    }

    public void StartStreaming(HSVPolePipeline.ViewportStage viewportStage)
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
        return 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public double AngleToClosestPole()
    {
        return 1;
    }
}

