package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        BNO055IMU imu;

        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        IMUparameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled       = true;
        IMUparameters.loggingTag           = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(IMUparameters);
        waitForStart();

        while(opModeIsActive())
        {

            telemetry.addData("Bruv", imu.getAngularOrientation().firstAngle);
            telemetry.update();
        }

    }
}
