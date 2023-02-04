package org.firstinspires.ftc.teamcode;

import android.app.PendingIntent;

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
    // Instance variables referencing the chassis' motors
    public DcMotorEx FrontRightWheel, FrontLeftWheel, BackRightWheel, BackLeftWheel;

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

    public Bot.State CalculateRotatedState(Vector2D input, Bot.State reference)
    {
        double encoderValue = ((input.angle * (28.7 * Math.PI)) / (9.6 * Math.PI)) * 537.7;

        reference.FrontRightEncoder -= encoderValue;
        reference.FrontLeftEncoder += encoderValue;
        reference.BackRightEncoder -= encoderValue;
        reference.BackLeftEncoder += encoderValue;

        return reference;
    }

    public Bot.State CalculateTranslatedState(Vector2D input, Bot.State reference)
    {
        double encoderValue = (input.magnitude / (9.6 * Math.PI)) * 537.7;

        reference.FrontRightEncoder += encoderValue;
        reference.FrontLeftEncoder += encoderValue;
        reference.BackRightEncoder += encoderValue;
        reference.BackLeftEncoder += encoderValue;

        return reference;
    }

    public void MoveAuto(Vector2D input, Bot bot)
    {
        PIDController PID = new PIDController(1, 1, 1);

        Bot.State targetState = bot.state;

        FrontRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (input.angle != 0)
        {
            targetState = CalculateRotatedState(input, bot.state);
        }
        else if (input.magnitude != 0)
        {
            targetState = CalculateTranslatedState(input, bot.state);
        }
        else return;

        while (!TargetStateReached(targetState, bot.state, 10))
        {
            double max;
            double frontLeftPower  = PID.Calculate(targetState.FrontRightEncoder, bot.state.FrontRightEncoder);
            double frontRightPower = PID.Calculate(targetState.FrontRightEncoder, bot.state.FrontRightEncoder);
            double backLeftPower   = PID.Calculate(targetState.FrontRightEncoder, bot.state.FrontRightEncoder);
            double backRightPower  = PID.Calculate(targetState.FrontRightEncoder, bot.state.FrontRightEncoder);

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
            bot.UpdateState();
        }
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

    public boolean TargetStateReached(Bot.State target, Bot.State current, double buffer)
    {
        return
                Math.abs(target.FrontRightEncoder - current.FrontRightEncoder) < buffer
                &&
                Math.abs(target.FrontLeftEncoder - current.FrontLeftEncoder) < buffer
                &&
                Math.abs(target.BackRightEncoder - current.BackRightEncoder) < buffer
                &&
                Math.abs(target.BackLeftEncoder - current.BackLeftEncoder) < buffer;
    }
}
