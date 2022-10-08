package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.SwerveDrive;
import org.firstinspires.ftc.teamcode.SwerveModule;
import org.firstinspires.ftc.teamcode.Vector2D;

@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        Bot bot = new Bot(MecanumDrive.class, hardwareMap);

        MecanumDrive chassis = (MecanumDrive)bot.chassis;

        Vector2D direction;

        waitForStart();

        while(opModeIsActive())
        {
            bot.UpdateIMUData(AxesReference.INTRINSIC, AxesOrder.XYZ);

            direction = new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y);

            chassis.Move(direction, gamepad1.right_stick_x, 1);

            telemetry.addData("IMU position ", bot.orientation);
            telemetry.addData("IMU acceleration ", bot.acceleration);
            telemetry.addData("IMU degrees ", bot.imu.getAngularOrientation());
            telemetry.addData("IMU gravity ", bot.imu.getGravity());
            telemetry.update();
        }

    }
}
