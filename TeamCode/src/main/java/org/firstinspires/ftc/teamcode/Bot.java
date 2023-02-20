package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.CameraInformation;
import org.firstinspires.ftc.robotcore.external.tfod.FrameConsumer;
import org.firstinspires.ftc.robotcore.external.tfod.FrameGenerator;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.utils.PoleDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

// This is the main class used for creating a reference to the robot globally
// It essentially acts as a wrapper for all our methods and functions so that it makes sense as a coherent program
// An instance of this class is required in every OpMode that you intend to actually use
public class Bot {

    static class State
    {
        public double IMUAngle;
    }
    public State state = new State();

    // The two major controllable components of the robot
    // Each of these components will utilize a different Gamepad during TeleOp
    public MecanumDrive chassis;
    public Turret turret;

    // Object references for the internal sensor array
    public BNO055IMU imu;

    // Webcam object for accessing camera data
    public OpenCvWebcam camera;
    public static TFObjectDetector tfod;
    public static VuforiaLocalizer vuforia;
    public static final String TFOD_MODEL_ASSET = "BlooBoi_Proto.tflite";
    public static final String[] LABELS = {"BLooboi"};
    private static final String VUFORIA_KEY = "AbskhHb/////AAABmb8nKWBiYUJ9oEFmxQL9H2kC6M9FzPa1acXUaS/H5wRkeNbpNVBJjDfcrhlTV2SIGc/lxBOtq9X7doE2acyeVOPg4sP69PQQmDVQH5h62IwL8x7BS/udilLU7MyX3KEoaFN+eR1o4FKBspsYrIXA/Oth+TUyrXuAcc6bKSSblICUpDXCeUbj17KrhghgcgxU6wzl84lCDoz6IJ9egO+CG4HlsBhC/YAo0zzi82/BIUMjBLgFMc63fc6eGTGiqjCfrQPtRWHdj2sXHtsjZr9/BpLDvFwFK36vSYkRoSZCZ38Fr+g3nkdep25+oEsmx30IkTYvQVMFZKpK3WWMYUWjWgEzOSvhh+3BOg+3UoxBJSNk";


    public Servo CameraServo;

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

        imu = hwMap.get(BNO055IMU.class, "EHUB_IMU");

        VuforiaLocalizer.Parameters Vparameters = new VuforiaLocalizer.Parameters();

        Vparameters.vuforiaLicenseKey = VUFORIA_KEY;
        Vparameters.cameraName = hwMap.get(WebcamName.class, "Eye_Of_Sauron");

        vuforia = ClassFactory.getInstance().createVuforia(Vparameters);

        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.setZoom(1, 16.0 / 9.0);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);

        camera = OpenCvCameraFactory.getInstance().createWebcam(hwMap.get(WebcamName.class, "Eye_Of_Sauron"), hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName()));
        CameraServo = hwMap.get(Servo.class, "CameraServo");
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
                camera.setPipeline(new PoleDetectionPipeline());
                camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                camera.startStreaming(1280, 720, OpenCvCameraRotation.SIDEWAYS_RIGHT);
                FtcDashboard.getInstance().startCameraStream(camera, 10);
            }

            // onError also need to be inlined, but isn't necessarily called when the camera is opened
            // Code isn't required inside onError cause it kills itself anyway
            @Override
            public void onError(int errorCode) {}
        });
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
        state.IMUAngle = imu.getAngularOrientation().firstAngle;
    }

}