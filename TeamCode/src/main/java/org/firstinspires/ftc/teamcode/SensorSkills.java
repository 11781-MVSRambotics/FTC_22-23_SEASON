package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class SensorSkills extends Bot {

    private static final double middlePixel = 322.5;

    // Debug Variables
    public double target;
    public double power;
    public int failed_attempts;

    public void UpdateIMUData (AxesReference frameOfReference, AxesOrder order)
    {
        orientation = imu.getAngularOrientation(frameOfReference, order, AngleUnit.DEGREES);
        acceleration = imu.getLinearAcceleration();
    }

    public void CameraNavigation (double error_margin)
    {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                // step through the list of recognitions and display boundary info.
                for (Recognition recognition : updatedRecognitions) {
                    // retrieves the deviation from the target
                    target = (recognition.getLeft() + recognition.getRight()) / 2;
                    if (recognition.getLabel().equalsIgnoreCase("BlooBoi")) failed_attempts = 0;
                }
            }
            else {failed_attempts += 1;}
        }

        if (failed_attempts >= 10)
        {
            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(0);
        }

        // Checks which direction to turn
        else if (target > middlePixel - error_margin && target < middlePixel + error_margin)
        {
            frontLeftMotor.setPower(0.25);
            frontRightMotor.setPower(0.25);
            backLeftMotor.setPower(0.25);
            backRightMotor.setPower(0.25);
        }

        else if (target < middlePixel - error_margin) {
            power = (1 / (middlePixel)) * (middlePixel - target);

            frontLeftMotor.setPower(0);
            frontRightMotor.setPower(power);
            backLeftMotor.setPower(0);
            backRightMotor.setPower(power);

        } else if (target > middlePixel + error_margin) {
            power = 1 / (645 - middlePixel) * (target - middlePixel);

            frontLeftMotor.setPower(power);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(power);
            backRightMotor.setPower(0);
        }
    }
}