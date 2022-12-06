package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.utils.Vector2D;

@SuppressWarnings("unused")
@TeleOp(name = "CompTeleOp")
public class CompTeleOp extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        Bot bot = new Bot(hardwareMap);
        MecanumDrive chassis = bot.chassis;
        Turret turret = bot.turret;
        double initialRobotAngle = bot.imu.getAngularOrientation().firstAngle;


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
            double joyStickAngle = Math.atan(gamepad1.left_stick_y / gamepad1.left_stick_x);
            double robotAngle = bot.orientation.firstAngle - initialRobotAngle;
            double properAngle = robotAngle - joyStickAngle;
            double moveX = Math.cos(properAngle * Math.PI/180);
            double moveY = Math.sin(properAngle * Math.PI/180);
            telemetry.addData("joystick angle", joyStickAngle);
            telemetry.addData("robot angle", robotAngle);
            telemetry.addData("proper angle", properAngle);


            chassis.Move(new Vector2D(moveX, moveY), gamepad1.right_stick_x, 1);

            if (gamepad1.right_bumper)
            {
                turret.Rotate(1, 5000, Turret.RotateMode.RELATIVE);
            }
            else if (gamepad1.left_bumper)
            {
                turret.Rotate(-1, 5000, Turret.RotateMode.RELATIVE);
            }

            telemetry.update();
        }

    }
}
