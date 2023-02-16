package org.firstinspires.ftc.teamcode;

import android.app.PendingIntent;
import android.provider.Telephony;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.robot.RobotState;

import org.firstinspires.ftc.teamcode.utils.PIDController;
import org.firstinspires.ftc.teamcode.utils.Vector2D;

import java.util.Vector;

// This class contains all the code required for traversing the field in both Auto and TeleOp
// An instance of this class is automatically created and initialized as a component of the Bot class
public class MecanumDrive
{
    public State currentState = new State();
    public State targetState = new State();
    // Instance variables referencing the chassis' motors
    public DcMotorEx FrontRightWheel, FrontLeftWheel, BackRightWheel, BackLeftWheel;

    public static class State
    {
        public double FrontRightPosition;
        public double FrontLeftPosition;
        public double BackRightPosition;
        public double BackLeftPosition;
    }

    // Constructor used to reverse the correct wheels to produce intuitive motion
    public MecanumDrive(DcMotorEx FrontRightMotor, DcMotorEx FrontLeftMotor, DcMotorEx BackRightMotor, DcMotorEx BackLeftMotor)
    {
        // Reversing the necessary motors so that the signs of power values match rotational direction
        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Configure motors so stop when not under load to avoid coasting during TeleOp
        FrontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Applying settings to instance variables to avoid confusion
        this.FrontRightWheel = FrontRightMotor;
        this.FrontLeftWheel = FrontLeftMotor;
        this.BackRightWheel = BackRightMotor;
        this.BackLeftWheel = BackLeftMotor;
    }

    // Main function for powering the motors
    // This is responsible for basically all chassis movement
    // All other logic typically occurs outside of this
    public void Move(Vector2D direction, double yaw, double power)
    {
        // Maximum power value so we can normalize the power vectors
        double max;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double frontLeftPower  = direction.y + direction.x + yaw;
        double frontRightPower = direction.y - direction.x - yaw;
        double backLeftPower   = direction.y - direction.x + yaw;
        double backRightPower  = direction.y + direction.x - yaw;

        // Check which motor has received the maximum power value
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        // Normalize all motor powers as a percentage of the maximum
        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }
        // Scale the normalized value to the desired power percentage
        frontLeftPower  *= power;
        frontRightPower *= power;
        backLeftPower   *= power;
        backRightPower  *= power;

        // Send calculated power to wheels
        FrontRightWheel.setPower(frontRightPower);
        FrontLeftWheel.setPower(frontLeftPower);
        BackRightWheel.setPower(backRightPower);
        BackLeftWheel.setPower(backLeftPower);
    }

    public void CalculateRotatedState(Vector2D input)
    {
        double encoderValue = ((input.angle * (28.7 * Math.PI)) / (9.6 * Math.PI)) * 537.7;

        targetState.FrontRightPosition = currentState.FrontRightPosition - encoderValue;
        targetState.FrontLeftPosition = currentState.FrontLeftPosition + encoderValue;
        targetState.BackRightPosition = currentState.BackRightPosition - encoderValue;
        targetState.BackLeftPosition = currentState.BackLeftPosition + encoderValue;
    }

    public void CalculateTranslatedState(Vector2D input)
    {
        double encoderValue = (input.magnitude / (9.6 * Math.PI)) * 537.7;

        targetState.FrontRightPosition = currentState.FrontRightPosition + encoderValue;
        targetState.FrontLeftPosition = currentState.FrontLeftPosition + encoderValue;
        targetState.BackRightPosition = currentState.BackRightPosition + encoderValue;
        targetState.BackLeftPosition = currentState.BackLeftPosition + encoderValue;
    }

    public void RotateAutoBasic(double angle, double power)
    {
        double encoderValue = ((angle * (28.7 * Math.PI)) / (9.6 * Math.PI)) * 537.7;

        FrontRightWheel.setTargetPosition((int) (FrontRightWheel.getCurrentPosition() - encoderValue));
        FrontLeftWheel.setTargetPosition((int) (FrontLeftWheel.getCurrentPosition() + encoderValue));
        BackRightWheel.setTargetPosition((int) (BackRightWheel.getCurrentPosition() - encoderValue));
        BackLeftWheel.setTargetPosition((int) (BackLeftWheel.getCurrentPosition() + encoderValue));

        while ( Math.abs(FrontRightWheel.getTargetPosition() - FrontRightWheel.getCurrentPosition()) < 30
                &&
                Math.abs(FrontLeftWheel.getTargetPosition() - FrontLeftWheel.getCurrentPosition()) < 30
                &&
                Math.abs(BackRightWheel.getTargetPosition() - BackRightWheel.getCurrentPosition()) < 30
                &&
                Math.abs(BackLeftWheel.getTargetPosition() - BackLeftWheel.getCurrentPosition()) < 30
        )
        {
            FrontRightWheel.setPower(power);
            FrontLeftWheel.setPower(power);
            BackRightWheel.setPower(power);
            BackLeftWheel.setPower(power);
        }
    }

    public void DriveAutoBasic (double distance, double power)
    {
        double encoderValue = (distance / (9.6 * Math.PI)) * 537.7;

        FrontRightWheel.setTargetPosition((int) (FrontRightWheel.getCurrentPosition() + encoderValue));
        FrontLeftWheel.setTargetPosition((int) (FrontLeftWheel.getCurrentPosition() + encoderValue));
        BackRightWheel.setTargetPosition((int) (BackRightWheel.getCurrentPosition() + encoderValue));
        BackLeftWheel.setTargetPosition((int) (BackLeftWheel.getCurrentPosition() + encoderValue));

        FrontRightWheel.setPower(power);
        FrontLeftWheel.setPower(power);
        BackRightWheel.setPower(power);
        BackLeftWheel.setPower(power);

        while ( Math.abs(FrontRightWheel.getTargetPosition() - FrontRightWheel.getCurrentPosition()) < 30
                &&
                Math.abs(FrontLeftWheel.getTargetPosition() - FrontLeftWheel.getCurrentPosition()) < 30
                &&
                Math.abs(BackRightWheel.getTargetPosition() - BackRightWheel.getCurrentPosition()) < 30
                &&
                Math.abs(BackLeftWheel.getTargetPosition() - BackLeftWheel.getCurrentPosition()) < 30
        )
        {
            FrontRightWheel.setPower(power);
            FrontLeftWheel.setPower(power);
            BackRightWheel.setPower(power);
            BackLeftWheel.setPower(power);
        }
    }

    public void MoveAuto(Vector2D input)
    {
        PIDController PID = new PIDController(1, 1, 1);

        FrontRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (input.angle != 0)
        {
            CalculateRotatedState(input);
        }
        else if (input.magnitude != 0)
        {
            CalculateTranslatedState(input);
        }
        else return;

        while (!TargetStateReached())
        {
            double max;
            double frontRightPower  = PID.Calculate(targetState.FrontRightPosition - currentState.FrontRightPosition);
            double frontLeftPower = PID.Calculate(targetState.FrontLeftPosition - currentState.FrontLeftPosition);
            double backRightPower   = PID.Calculate(targetState.BackRightPosition - currentState.BackRightPosition);
            double backLeftPower  = PID.Calculate(targetState.BackLeftPosition - currentState.BackLeftPosition);

            // Check which motor has received the maximum power value
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            // Normalize all motor powers as a percentage of the maximum
            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            FrontRightWheel.setPower(frontRightPower);
            FrontLeftWheel.setPower(frontLeftPower);
            BackRightWheel.setPower(backRightPower);
            BackLeftWheel.setPower(backLeftPower);
        }
    }

    public boolean TargetStateReached()
    {
        return
                targetState.FrontRightPosition - currentState.FrontRightPosition < 10
                &&
                targetState.FrontLeftPosition - currentState.FrontLeftPosition < 10
                &&
                targetState.BackRightPosition - currentState.BackRightPosition < 10
                &&
                targetState.BackLeftPosition - currentState.BackLeftPosition < 10;


    }

    public void UpdateCurrentState()
    {
        currentState.FrontRightPosition = FrontRightWheel.getCurrentPosition();
        currentState.FrontLeftPosition = FrontLeftWheel.getCurrentPosition();
        currentState.BackRightPosition = BackRightWheel.getCurrentPosition();
        currentState.BackLeftPosition = BackLeftWheel.getCurrentPosition();
    }
}
