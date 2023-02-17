package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Turret;
import org.firstinspires.ftc.teamcode.utils.Vector2D;
import org.opencv.core.Mat;

@SuppressWarnings("unused")
@TeleOp(name = "CompTeleOp")
public class CompTeleOp extends LinearOpMode
{
    @Override
    public void runOpMode() {
        Bot bot = new Bot(hardwareMap);
        MecanumDrive chassis = bot.chassis;
        Turret turret = bot.turret;


        double initialAngle = 0;
        while (!bot.imu.isGyroCalibrated())
        {
            initialAngle = bot.imu.getAngularOrientation().firstAngle;
            telemetry.addData("Gyro is calibrating: ", bot.imu.isGyroCalibrated());
        }

        waitForStart();

        while(opModeIsActive()) {
            Vector2D input = Vector2D.ConstructFromComponents(gamepad1.left_stick_x, gamepad1.left_stick_y);

            input = Vector2D.rotate(input, -(((bot.imu.getAngularOrientation().firstAngle - initialAngle + 90) / 180) * Math.PI));

            // Chassis controller
            chassis.Move(input, gamepad1.right_stick_x, 1);

            if (gamepad1.dpad_up)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((Math.PI/2), 2), 0, 0.2);
            }
            else if (gamepad1.dpad_right)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((0), 2), 0, 0.2);

            }
            else if (gamepad1.dpad_down)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((3 * Math.PI / 2), 2), 0, 0.2);

            }
            else if (gamepad1.dpad_left)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((Math.PI), 2), 0, 0.2);

            }

            if (gamepad1.a)
            {
                initialAngle = bot.imu.getAngularOrientation().firstAngle;
            }

            if (gamepad2.a)
            {
                bot.turret.AddArmInput(1);

            }
            else if (gamepad2.b)
            {
                bot.turret.AddArmInput(0);
            }
            else
            {
                bot.turret.AddArmInput(0.5);
            }


            bot.Move();
            bot.UpdateState();

            telemetry.addData("Initial Angle: ", initialAngle);
            telemetry.addData("Current bot heading", bot.imu.getAngularOrientation().firstAngle);
            telemetry.addData("Real angle", bot.imu.getAngularOrientation().firstAngle - initialAngle);
            telemetry.update();
        }

    }
}
