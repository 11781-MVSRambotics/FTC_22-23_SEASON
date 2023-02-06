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

        turret.ExtendMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.TurnMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        turret.ExtendMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turret.TurnMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
                chassis.Move(Vector2D.ConstructFromAngleAndMag((Math.PI/2), 1), 0, 0.1);
            }
            else if (gamepad1.dpad_right)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((0), 1), 0, 0.1);

            }
            else if (gamepad1.dpad_down)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((3 * Math.PI / 2), 1), 0, 0.1);

            }
            else if (gamepad1.dpad_left)
            {
                chassis.Move(Vector2D.ConstructFromAngleAndMag((Math.PI), 1), 0, 0.1);

            }

            if (gamepad1.a)
            {
                initialAngle = bot.imu.getAngularOrientation().firstAngle;
            }

            // Turret Controller
            if (gamepad1.right_trigger > 0)
            {
                turret.TurnMotor.setPower(gamepad1.right_trigger);
            }
            else if (gamepad1.left_trigger > 0)
            {
                turret.TurnMotor.setPower(-gamepad1.left_trigger);
            }
            else
            {
                turret.TurnMotor.setPower(0);
            }

            if (gamepad2.right_trigger > 0 && !bot.SlideLimitSwitch.getState())
            {
                turret.ExtendMotor.setPower(gamepad2.right_trigger);
            }
            else if (gamepad2.left_trigger > 0)
            {
                turret.ExtendMotor.setPower(-gamepad2.left_trigger);
            }
            else
            {
                turret.ExtendMotor.setPower(0);
            }

            if (gamepad2.dpad_up)
            {
                bot.RightArmServo.setPower(-1);
                bot.LeftArmServo.setPower(1);
            }
            else if (gamepad2.dpad_down)
            {
                bot.RightArmServo.setPower(1);
                bot.LeftArmServo.setPower(-1);
            }
            else
            {
                bot.RightArmServo.setPower(0);
                bot.LeftArmServo.setPower(0);
            }

            if (gamepad2.a) bot.ClawServo.setPower(-1);
            else if (gamepad2.b) bot.ClawServo.setPower(1);
            else bot.ClawServo.setPower(0);

            telemetry.addData("LimitSwitch", bot.SlideLimitSwitch.getState());
            telemetry.addData("Initial Angle: ", initialAngle);
            telemetry.addData("Current bot heading", bot.imu.getAngularOrientation().firstAngle);
            telemetry.addData("Real angle", bot.imu.getAngularOrientation().firstAngle - initialAngle);
            telemetry.addData("Claw power", bot.ClawServo.getPower());
            telemetry.update();
        }

    }
}
