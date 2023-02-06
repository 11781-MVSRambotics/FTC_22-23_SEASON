package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.Robot;

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

        // Motor
        public double FrontRightEncoder;
        public double FrontLeftEncoder;
        public double BackRightEncoder;
        public double BackLeftEncoder;
        public double TurnTableEncoder;
        public double SlideExtensionEncoder;
        public double SlideRetractionEncoder;

        // Servo
        public double ArmEncoder;
        public double WristEncoder;
        public double ClawEncoder;

        // Other
        public double IMUAngle;
    }

    public State state;

    // The two major controllable components of the robot
    // Each of these components will utilize a different Gamepad during TeleOp
    public MecanumDrive chassis;
    public Turret turret;

    // Individual controllable hardware components
    public DcMotorEx FrontRightMotor, FrontLeftMotor, BackRightMotor, BackLeftMotor, TurretTurnMotor, TurretExtendMotor;
    public CRServo LeftArmServo, RightArmServo, ClawServo;

    // Object references for the internal sensor array
    public BNO055IMU imu;
    public Orientation orientation;
    public Acceleration acceleration;

    // Webcam object for accessing camera data
    public OpenCvWebcam camera;

    public DigitalChannel SlideLimitSwitch;

    // Constructor that runs each time an object belonging to this class is created
    // All code necessary for startup (pre-opmode) is placed here
    // Accepts the global HardwareMap as a parameter for accessing physical devices
    public Bot (HardwareMap hwMap)
    {
        // Linking class objects to physical devices listed in the configuration
        // The configuration is specified in the DriverStation app
        FrontRightMotor = hwMap.get(DcMotorEx.class, "FrontRightMotor");
        FrontLeftMotor = hwMap.get(DcMotorEx.class, "FrontLeftMotor");
        BackRightMotor = hwMap.get(DcMotorEx.class, "BackRightMotor");
        BackLeftMotor = hwMap.get(DcMotorEx.class, "BackLeftMotor");
        TurretTurnMotor = hwMap.get(DcMotorEx.class, "TurretSpinMotor");
        TurretExtendMotor = hwMap.get(DcMotorEx.class, "TurretExtendMotor");

        ClawServo = hwMap.get(CRServo.class, "ClawServo");
        LeftArmServo = hwMap.get(CRServo.class, "LeftArmServo");
        RightArmServo = hwMap.get(CRServo.class, "RightArmServo");

        // Instantiating chassis object
        // All global movement code resides in this object
        // Accepts four motor objects as a reference to each wheel
        chassis = new MecanumDrive(FrontRightMotor, FrontLeftMotor, BackRightMotor, BackLeftMotor);

        // Instantiating turret object
        // Responsible for all cone manipulation
        // Accepts two motor objects
        turret = new Turret(TurretTurnMotor, TurretExtendMotor);

        // Linking instance variable to physical device
        imu = hwMap.get(BNO055IMU.class, "imu");

        // Virtual link to the phone to allow for a preview of the camera's FOV during initialization
        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        // Creating a camera object in accordance with library constraints and linking to physical device
        camera = OpenCvCameraFactory.getInstance().createWebcam(hwMap.get(WebcamName.class, "Eye_Of_Sauron"), cameraMonitorViewId);

        SlideLimitSwitch = hwMap.get(DigitalChannel.class, "SlideUpperLimitSwitch");

        // A container class for all the initialization data that eventually gets used to configure the imu
        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        // Configuring imu settings
        IMUparameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled       = true;
        IMUparameters.loggingTag           = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

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

    // Rechecks the imu and updates the position data of the bot object to avoid needing to access the imu directly
    public void UpdateIMUData(AxesReference frameOfReference, AxesOrder order)
    {
        orientation = imu.getAngularOrientation(frameOfReference, order, AngleUnit.DEGREES);
        acceleration = imu.getLinearAcceleration();
    }

    public void UpdateState()
    {
        // Motor
        state.FrontRightEncoder = FrontRightMotor.getCurrentPosition();
        state.FrontLeftEncoder = FrontLeftMotor.getCurrentPosition();
        state.BackRightEncoder = BackRightMotor.getCurrentPosition();
        state.BackLeftEncoder = BackLeftMotor.getCurrentPosition();
        state.TurnTableEncoder = TurretTurnMotor.getCurrentPosition();
        state.SlideExtensionEncoder = TurretExtendMotor.getCurrentPosition();
        state.SlideRetractionEncoder = 0;

        // Servo
        state.ArmEncoder = 0;
        state.WristEncoder = 0;
        state.ClawEncoder = 0;

        // Other
        state.IMUAngle = imu.getAngularOrientation().firstAngle;
    }

}