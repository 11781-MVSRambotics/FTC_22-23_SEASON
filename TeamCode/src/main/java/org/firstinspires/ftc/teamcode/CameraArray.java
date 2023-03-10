package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utils.HSVPolePipeline;
import org.opencv.calib3d.Calib3d;
import org.opencv.calib3d.StereoSGBM;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

import java.util.ArrayList;
import java.util.List;

public class CameraArray
{
    static final int STREAM_WIDTH = 960;
    static final int STREAM_HEIGHT = 720;

    static HSVPolePipeline rightPipeline = new HSVPolePipeline();
    static HSVPolePipeline leftPipeline = new HSVPolePipeline();

    OpenCvCamera rightCamera, leftCamera;

    public CameraArray(WebcamName RightCamera, WebcamName LeftCamera, int ViewID)
    {
        if (ViewID == -1)
        {
            this.rightCamera = OpenCvCameraFactory.getInstance().createWebcam(RightCamera);
            this.leftCamera = OpenCvCameraFactory.getInstance().createWebcam(LeftCamera);
            return;
        }

        int[] ViewIDs = OpenCvCameraFactory.getInstance().splitLayoutForMultipleViewports(ViewID, 2, OpenCvCameraFactory.ViewportSplitMethod.HORIZONTALLY);
        this.rightCamera = OpenCvCameraFactory.getInstance().createWebcam(RightCamera, ViewIDs[0]);
        this.leftCamera = OpenCvCameraFactory.getInstance().createWebcam(LeftCamera, ViewIDs[1]);
    }

    public void setViewportStage(HSVPolePipeline.ViewportStage stage)
    {
        rightPipeline.setViewportStage(stage);
        leftPipeline.setViewportStage(stage);
    }

    public void startStreaming()
    {
        rightCamera.setPipeline(rightPipeline);
        leftCamera.setPipeline(leftPipeline);

        rightCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() { rightCamera.startStreaming(STREAM_WIDTH, STREAM_HEIGHT); }
            @Override
            public void onError(int errorCode) {}
        });
        leftCamera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened() { leftCamera.startStreaming(STREAM_WIDTH, STREAM_HEIGHT); }
            @Override
            public void onError(int errorCode) {}
        });
    }
}

