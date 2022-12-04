package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Vector2D;

@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {

        DcMotorEx FrontRightMotor = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        DcMotorEx FrontLeftMotor = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        DcMotorEx BackRightMotor = hardwareMap.get(DcMotorEx.class, "BackRightMotor");
        DcMotorEx BackLeftMotor = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");

        BNO055IMU imu;
        BNO055IMU extimu;

        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        IMUparameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled       = true;
        IMUparameters.loggingTag           = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        extimu = hardwareMap.get(BNO055IMU.class, "extimu");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(IMUparameters);

        MecanumDrive chassis = new MecanumDrive(FrontRightMotor, FrontLeftMotor, BackRightMotor, BackLeftMotor, imu);

        waitForStart();

        while(opModeIsActive())
        {
            chassis.Move(new Vector2D(gamepad1.left_stick_x, -gamepad1.left_stick_y), gamepad1.right_stick_x, 1);


            telemetry.addData("FrontLeft", FrontLeftMotor.getPower());
            telemetry.addData("FrontRight", FrontRightMotor.getPower());
            telemetry.addData("BackLeft", BackLeftMotor.getPower());
            telemetry.addData("BackRight", BackRightMotor.getPower());
            telemetry.addData("Joystick y:", gamepad1.left_stick_y);
            telemetry.addData("Joystick x:", gamepad1.left_stick_x);
            telemetry.addData("Right JOystick", gamepad1.right_stick_x);

            telemetry.addData("Bruv", extimu.getAngularOrientation());
            telemetry.update();
        }

    }
}
