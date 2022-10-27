package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Bot {

    public Drivetrain chassis;

    public BNO055IMU imu;
    public UltrasonicSensor ultrasonicSensor;

    public Orientation orientation;
    public Acceleration acceleration;



    public Bot (Class<? extends Drivetrain> drivetrainClass, HardwareMap hwMap)
    {
        if (drivetrainClass == SwerveDrive.class)
        {
            DcMotorEx RightTopSwerveMotor = hwMap.get(DcMotorEx.class, "RightTopSwerve");
            DcMotorEx RightBottomSwerveMotor = hwMap.get(DcMotorEx.class, "RightBottomSwerve");
            DcMotorEx LeftTopSwerveMotor = hwMap.get(DcMotorEx.class, "LeftTopSwerve");
            DcMotorEx LeftBottomSwerveMotor = hwMap.get(DcMotorEx.class, "LeftBottomSwerve");

            SwerveModule RightSwerveModule = new SwerveModule(RightTopSwerveMotor, RightBottomSwerveMotor);
            SwerveModule LeftSwerveModule = new SwerveModule(LeftTopSwerveMotor, LeftBottomSwerveMotor);

             chassis = new SwerveDrive(RightSwerveModule, LeftSwerveModule);
        }

        else if (drivetrainClass == MecanumDrive.class)
        {
            DcMotorEx FrontRightMotor = hwMap.get(DcMotorEx.class, "FrontRightMotor");
            DcMotorEx FrontLeftMotor = hwMap.get(DcMotorEx.class, "FrontLeftMotor");
            DcMotorEx BackRightMotor = hwMap.get(DcMotorEx.class, "BackRightMotor");
            DcMotorEx BackLeftMotor = hwMap.get(DcMotorEx.class, "BackLeftMotor");

            FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            BackRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

            chassis = new MecanumDrive(FrontRightMotor, FrontLeftMotor, BackRightMotor, BackLeftMotor);
        }

        this.ultrasonicSensor = new UltrasonicSensor(hwMap.get(DigitalChannel.class, "US_Trigger"), hwMap.get(DigitalChannel.class, "US_Echo"));

        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        IMUparameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled       = true;
        IMUparameters.loggingTag           = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(IMUparameters);
    }

    public void UpdateIMUData (AxesReference frameOfReference, AxesOrder order)
    {
        orientation = imu.getAngularOrientation(frameOfReference, order, AngleUnit.DEGREES);
        acceleration = imu.getLinearAcceleration();
    }

}





















