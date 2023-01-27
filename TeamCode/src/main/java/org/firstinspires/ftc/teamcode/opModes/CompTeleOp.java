package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

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

        double initialRobotAngle = 0;
        double joystickAngle;
        double robotAngle;
        double properAngle;
        double moveX;
        double moveY;

        waitForStart();

        bot.UpdateIMUData();
        do
        {
            bot.UpdateIMUData();
            initialRobotAngle = bot.orientation.firstAngle;
        } while (!bot.imu.isGyroCalibrated());

        while(opModeIsActive())
        {
            if (gamepad1.left_stick_x > 0) {
                joystickAngle = Math.atan(gamepad1.left_stick_y / gamepad1.left_stick_x);
            }
            else {
                joystickAngle = Math.atan(gamepad1.left_stick_y / gamepad1.left_stick_x) + (Math.PI / 2);
            }

            bot.UpdateIMUData();

            robotAngle = bot.orientation.firstAngle - initialRobotAngle;
            properAngle = robotAngle - joystickAngle;
            moveX = Math.cos(properAngle * Math.PI/180);
            moveY = Math.sin(properAngle * Math.PI/180);

            telemetry.addData("initial angle", initialRobotAngle);
            telemetry.addData("Gyro", bot.orientation.firstAngle);
            telemetry.addData("joystick angle", joystickAngle);
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
