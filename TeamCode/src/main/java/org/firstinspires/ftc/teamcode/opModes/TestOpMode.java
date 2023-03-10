package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CameraArray;
import org.firstinspires.ftc.teamcode.utils.HSVPolePipeline;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvSwitchableWebcam;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{

    Mat disparityMap = new Mat();

    @Override
    public void runOpMode()
    {
        CameraArray cameras = new CameraArray(
                hardwareMap.get(WebcamName.class, "Webcam 1"),
                hardwareMap.get(WebcamName.class, "Webcam 2"),
                hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName())
        );

        cameras.setViewportStage(HSVPolePipeline.ViewportStage.HSV);
        cameras.startStreaming();

        waitForStart();

        while(opModeIsActive())
        {
            telemetry.update();
        }

    }
}
