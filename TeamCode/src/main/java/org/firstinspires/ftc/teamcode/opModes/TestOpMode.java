package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.utils.SummitsPipeline;
import org.firstinspires.ftc.teamcode.utils.HSVPolePipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;

@SuppressWarnings("unused")
@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {

                SummitsPipeline summitsPipeline = new SummitsPipeline();
                HSVPolePipeline ourPipeline = new HSVPolePipeline();

                ourPipeline.setViewportStage(HSVPolePipeline.ViewportStage.POLES);

                camera.setPipeline(ourPipeline);
                camera.startStreaming(1280, 720);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        waitForStart();

        while(opModeIsActive())
        {
            telemetry.update();
        }

    }
}
