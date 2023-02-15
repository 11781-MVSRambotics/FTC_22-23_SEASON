package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.utils.PoleDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvWebcam;

// This is the main class used for creating a reference to the robot globally
// It essentially acts as a wrapper for all our methods and functions so that it makes sense as a coherent program
// An instance of this class is required in every OpMode that you intend to actually use
public class Bot {

    static class State
    {
        public double IMUAngle;
    }
    public State state;

    // The two major controllable components of the robot
    // Each of these components will utilize a different Gamepad during TeleOp
    public MecanumDrive chassis;
    public Turret turret;

    // Object references for the internal sensor array
    public BNO055IMU imu;

    // Webcam object for accessing camera data
    public OpenCvWebcam camera;

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
                hwMap.get(DcMotorEx.class, "TurretExtendMotor"),
                hwMap.get(Servo.class, "RightArmServo"),
                hwMap.get(Servo.class, "LeftArmServo"),
                hwMap.get(Servo.class, "RightClawServo"),
                hwMap.get(Servo.class, "LeftClawServo"),
                hwMap.get(DigitalChannel.class, "SlideLimiter")
        );

        imu = hwMap.get(BNO055IMU.class, "imu");

        camera = OpenCvCameraFactory.getInstance().createWebcam(hwMap.get(WebcamName.class, "Eye_Of_Sauron"), hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName()));

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

        // Open the virtual pathway to the camera hardware allowing for it to be started or configured
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {

            // onOpened runs the immediately after openCameraDeviceAsync is called, which is why it must be inline
            @Override
            public void onOpened() {
                // The pipeline is the set of operations that occurs before the image is presented to the user
                // Sets the camera pipeline to a custom one for pole detection
                camera.setPipeline(new PoleDetectionPipeline());

                //Actually turn the camera on so it will process images
                //camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            // onError also need to be inlined, but isn't necessarily called when the camera is opened
            // Code isn't required inside onError cause it kills itself anyway
            @Override
            public void onError(int errorCode) {}
        });
    }

    public void Move()
    {
        turret.Move();
    }

    public void UpdateState()
    {
        chassis.UpdateCurrentState();
        turret.UpdateCurrentState();

        // Other
        state.IMUAngle = imu.getAngularOrientation().firstAngle;
    }

}