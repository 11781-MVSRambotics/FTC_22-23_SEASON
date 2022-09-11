package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class Bot {

        // Motors
        public static DcMotorEx frontLeftMotor = null;
        public static DcMotorEx frontRightMotor = null;
        public static DcMotorEx backLeftMotor = null;
        public static DcMotorEx backRightMotor = null;
        public static DcMotorEx armMotor = null;
        public static DcMotorEx spinMotor = null;

        // Servos
        public static Servo wristServo = null;
        public static Servo clawServo = null;

        // Safety features
        public static double chassis_speed_cap = 0.5;
        public static double arm_speed_cap = 0.5;
        public static double spinner_speed_cap = 0.5;

        // Navigation sensors
        public static WebcamName webcam = null;
        public static BNO055IMU imu = null;
        public static Orientation orientation = null;
        public static Acceleration acceleration = null;

        // Camera parameters
        public static TFObjectDetector tfod;
        public static VuforiaLocalizer vuforia;
        private static final String TFOD_MODEL_ASSET = "BlooBoi_Proto.tflite";
        private static final String[] LABELS = {"Blooboi"};
        private static final String VUFORIA_KEY = "AbskhHb/////AAABmb8nKWBiYUJ9oEFmxQL9H2kC6M9FzPa1acXUaS/H5wRkeNbpNVBJjDfcrhlTV2SIGc/lxBOtq9X7doE2acyeVOPg4sP69PQQmDVQH5h62IwL8x7BS/udilLU7MyX3KEoaFN+eR1o4FKBspsYrIXA/Oth+TUyrXuAcc6bKSSblICUpDXCeUbj17KrhghgcgxU6wzl84lCDoz6IJ9egO+CG4HlsBhC/YAo0zzi82/BIUMjBLgFMc63fc6eGTGiqjCfrQPtRWHdj2sXHtsjZr9/BpLDvFwFK36vSYkRoSZCZ38Fr+g3nkdep25+oEsmx30IkTYvQVMFZKpK3WWMYUWjWgEzOSvhh+3BOg+3UoxBJSNk";

        // Analog ports
        public static AnalogOutputController analogController1;

    public static void initializeHWMap (HardwareMap hwMap){

        //#################### HARDWARE MAPPING ####################\\
        frontLeftMotor = hwMap.get(DcMotorEx.class, "front_left_motor");
        frontRightMotor = hwMap.get(DcMotorEx.class, "front_right_motor");
        backLeftMotor = hwMap.get(DcMotorEx.class, "back_left_motor");
        backRightMotor = hwMap.get(DcMotorEx.class, "back_right_motor");
        armMotor = hwMap.get(DcMotorEx.class, "swing_arm_motor");
        spinMotor = hwMap.get(DcMotorEx.class, "spin_motor");

        wristServo = hwMap.get(Servo.class, "wrist_joint");
        clawServo = hwMap.get(Servo.class, "claw_servo");

        imu = hwMap.get(BNO055IMU.class, "imu");
        webcam = hwMap.get(WebcamName.class, "Webcam 1");

        analogController1 = hwMap.get(AnalogOutputController.class, "analog_port_1");
        //#################### HARDWARE MAPPING END ####################\\

        //#################### IMU CALIBRATION ####################\\
        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        IMUparameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled       = true;
        IMUparameters.loggingTag           = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu.initialize(IMUparameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 500);
        //#################### IMU CALIBRATION END ####################\\

        //#################### VUFORIA INITIALIZATION ####################\\
        VuforiaLocalizer.Parameters Vparameters = new VuforiaLocalizer.Parameters();

        Vparameters.vuforiaLicenseKey = VUFORIA_KEY;
        Vparameters.cameraName = webcam;

        vuforia = ClassFactory.getInstance().createVuforia(Vparameters);
        //#################### VUFORIA INITIALIZATION ENDS ####################\\

        //#################### TFOD INITIALIZATION ####################\\
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.setZoom(1, 16.0 / 9.0);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        //#################### TFOD INITIALIZATION END ####################\\

        //#################### SETTING RUNMODES ####################\\
        frontLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        armMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        spinMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        frontLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        armMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        spinMotor.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        //#################### SETTING RUNMODES END ####################\\

        //#################### SETTING DIRECTIONS ####################\\
        frontLeftMotor.setDirection((DcMotorSimple.Direction.FORWARD));
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //#################### SETTING DIRECTIONS END ####################\\

        //#################### HALT BEHAVIOR ####################\\
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //#################### HALT BEHAVIOR END ####################\\

        //#################### STOP DURING INITIALIZATION ####################\\
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        backLeftMotor.setPower(0);
        backRightMotor.setPower(0);
        //#################### STOP DURING INITIALIZATION END ####################\\
    }

}