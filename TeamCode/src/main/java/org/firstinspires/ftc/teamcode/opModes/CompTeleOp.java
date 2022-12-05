package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;
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
        MecanumDrive chassis = bot.chassis;
        Turret turret = bot.turret;



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

        bot.UpdateIMUData(AxesReference.EXTRINSIC, AxesOrder.XYZ);
        double initialRobotAngle = bot.orientation.firstAngle;

        while(opModeIsActive())
        {
            bot.UpdateIMUData(AxesReference.EXTRINSIC, AxesOrder.XYZ);
            double joystickAngle = 0;
            if (gamepad1.left_stick_x > 0) {
                joystickAngle = Math.atan(gamepad1.left_stick_y / gamepad1.left_stick_x);
            }
            else {
                joystickAngle = Math.atan(gamepad1.left_stick_y / gamepad1.left_stick_x) + (Math.PI / 2);
            }
            double robotAngle = bot.orientation.firstAngle - initialRobotAngle;
            double properAngle = robotAngle - joystickAngle;
            double moveX = Math.cos(properAngle * Math.PI/180);
            double moveY = Math.sin(properAngle * Math.PI/180);
            telemetry.addData("Gyro", bot.orientation.firstAngle);
            telemetry.addData("joystick angle", joystickAngle);
            telemetry.addData("initial angle", initialRobotAngle);
            telemetry.addData("robot angle", robotAngle);
            telemetry.addData("proper angle", properAngle);
            telemetry.addData("RSX", gamepad1.right_stick_x);


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