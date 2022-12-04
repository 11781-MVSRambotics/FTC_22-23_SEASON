package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class Bot {

    public MecanumDrive chassis;
    public Turret turret;
    public OpenCvWebcam camera;
    public BNO055IMU imu;
    public Orientation orientation;
    public Acceleration acceleration;


    public Bot (HardwareMap hwMap)
    {

        DcMotorEx FrontRightMotor = hwMap.get(DcMotorEx.class, "FrontRightMotor");
        DcMotorEx FrontLeftMotor = hwMap.get(DcMotorEx.class, "FrontLeftMotor");
        DcMotorEx BackRightMotor = hwMap.get(DcMotorEx.class, "BackRightMotor");
        DcMotorEx BackLeftMotor = hwMap.get(DcMotorEx.class, "BackLeftMotor");
        DcMotorEx TurretTurnMotor = hwMap.get(DcMotorEx.class, "TurretSpinMotor");
        DcMotorEx TurretExtendMotor = hwMap.get(DcMotorEx.class, "TurretExtendMotor");

        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        chassis = new MecanumDrive(FrontRightMotor, FrontLeftMotor, BackRightMotor, BackLeftMotor);
        turret = new Turret(TurretTurnMotor, TurretExtendMotor);

        BNO055IMU.Parameters IMUparameters = new BNO055IMU.Parameters();

        IMUparameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        IMUparameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        IMUparameters.calibrationDataFile  = "BNO055IMUCalibration.json";
        IMUparameters.loggingEnabled       = true;
        IMUparameters.loggingTag           = "IMU";
        IMUparameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(IMUparameters);

        int cameraMonitorViewId = hwMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hwMap.get(WebcamName.class, "Eye_Of_Sauron"), cameraMonitorViewId);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.setPipeline(new PoleDetectionPipeline());
                //camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    public void UpdateIMUData (AxesReference frameOfReference, AxesOrder order)
    {
        orientation = imu.getAngularOrientation(frameOfReference, order, AngleUnit.DEGREES);
        acceleration = imu.getLinearAcceleration();
    }

}





















