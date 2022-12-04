package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.utils.PoleDetectionPipeline;
import org.firstinspires.ftc.teamcode.utils.Vector2D;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp(name = "CompTeleOp")
public class CompTeleOp extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        Bot bot = new Bot(hardwareMap);

        /*
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        OpenCvWebcam camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Eye_Of_Sauron"), cameraMonitorViewId);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.setPipeline(new PoleDetectionPipeline());
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
         */

        waitForStart();

        while(opModeIsActive())
        {
            bot.chassis.Move(new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y), gamepad1.right_stick_x, 1);

            // bot.chassis.BackRightWheel.setPower(gamepad1.left_stick_x); //b
            // bot.chassis.BackLeftWheel.setPower(gamepad1.left_stick_y); //b
            // bot.chassis.FrontRightWheel.setPower(gamepad1.right_stick_x);
            // bot.chassis.FrontLeftWheel.setPower(gamepad1.right_stick_y);
            if (gamepad1.right_trigger != 0)
            {
                bot.turret.Extend(1000, 2000, Turret.ExtendMode.ABSOLUTE);
            }
            else
            {
                bot.turret.Extend(0, 2000, Turret.ExtendMode.ABSOLUTE);
            }

            if (gamepad1.right_bumper)
            {
                bot.turret.Rotate(480, 500, Turret.RotateMode.ABSOLUTE);
            }

            else if (gamepad1.left_bumper)
            {
                bot.turret.Rotate(-480, 500, Turret.RotateMode.ABSOLUTE);
            }

            else
            {
                bot.turret.Rotate(0, 500, Turret.RotateMode.ABSOLUTE);
            }

            telemetry.update();
        }

    }
}
