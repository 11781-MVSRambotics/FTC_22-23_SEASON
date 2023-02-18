package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.vuforia.Frame;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.CameraInformation;
import org.firstinspires.ftc.robotcore.external.tfod.FrameConsumer;
import org.firstinspires.ftc.robotcore.external.tfod.FrameGenerator;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.utils.PoleDetectionPipeline;
import org.firstinspires.ftc.teamcode.utils.Vector2D;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.List;

@SuppressWarnings("unused")
@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        Bot bot = new Bot(hardwareMap);

        waitForStart();

        while(opModeIsActive())
        {
            if (Bot.tfod != null)
            {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = Bot.tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null)
                {
                    // step through the list of recognitions and display boundary info.
                    for (Recognition recognition : updatedRecognitions)
                    {
                        // retrieves the deviation from the target
                        switch (recognition.getLabel())
                        {
                            case "Bloo":
                                telemetry.addData("It's bloo ", 0);
                                break;
                            case "Geen":
                                telemetry.addData("It's geen ", 0);
                                break;
                            case "Red":
                                telemetry.addData("It's red ", 0);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            telemetry.update();
        }

    }
}
