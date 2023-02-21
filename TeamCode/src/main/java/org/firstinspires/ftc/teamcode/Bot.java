package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

// This is the main class used for creating a reference to the robot globally
// It essentially acts as a wrapper for all our methods and functions so that it makes sense as a coherent program
// An instance of this class is required in every OpMode that you intend to actually use
public class Bot {

    static class State
    {
        public double IMUAngle;
    }

    public State currentState = new State();
    public State targetState = new State();

    // The two major controllable components of the robot
    // Each of these components will utilize a different Gamepad during TeleOp
    public MecanumDrive chassis;
    public Turret turret;
    public CameraArray cameras;

    // Object references for the internal sensor array
    public BNO055IMU imu;

    // Constructor that runs each time an object belonging to this class is created
    // All code necessary for startup (pre-opmode) is placed here
    // Accepts the global HardwareMap as a parameter for accessing physical devices
    public Bot (HardwareMap hwMap)
    {
        chassis = new MecanumDrive(
                hwMap.get(DcMotorEx.class, "FrontRightMotor"),
                hwMap.get(DcMotorEx.class, "FrontLeftMotor"),
                hwMap.get(DcMotorEx.class, "BackRightMotor"),
                hwMap.get(DcMotorEx.class, "BackLeftMotor")
        );

        turret = new Turret(
                hwMap.get(DcMotorEx.class, "TurretSpinMotor"),
                hwMap.get(DcMotorEx.class, "RightTurretExtendMotor"),
                hwMap.get(DcMotorEx.class, "LeftTurretExtendMotor"),
                hwMap.get(CRServo.class, "RightArmServo"),
                hwMap.get(CRServo.class, "LeftArmServo"),
                hwMap.get(Servo.class, "RightClawServo"),
                hwMap.get(Servo.class, "LeftClawServo"),
                hwMap.get(DigitalChannel.class, "SlideLimiter")
        );

        cameras = new CameraArray(
                hwMap.get(WebcamName.class, "RightCamera"),
                hwMap.get(WebcamName.class, "LeftCamera"),
                hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName())
        );

        imu = hwMap.get(BNO055IMU.class, "EHUB_IMU");

        // A container class for all the initialization data that eventually gets used to configure the imu
        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        // Configuring imu settings
        IMUparameters.angleUnit                         = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit                         = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile               = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled                    = true;
        IMUparameters.loggingTag                        = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm  = new JustLoggingAccelerationIntegrator();

        // Send the configured settings to the imu and start it
        imu.initialize(IMUparameters);
    }

    public void Move()
    {
        UpdateState();
        turret.Move();
        //chassis.Move();
    }

    public void UpdateState()
    {
        turret.UpdateCurrentState();
        chassis.UpdateCurrentState();

        // Other
        currentState.IMUAngle = imu.getAngularOrientation().firstAngle;
    }

}