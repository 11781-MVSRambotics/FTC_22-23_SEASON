package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

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


    public Bot.State CalulateTargetState(Vector2D input, Bot.State currentState)
    {


        return new Bot.State();
    }

    public void MoveAuto(Bot.State targetState, Bot bot)
    {
        FrontRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (targetState != bot.state)
        {
            FrontRightWheel.setPower(PIDController.Calculate(targetState.FrontRightEncoder, bot.state.FrontRightEncoder));
            FrontLeftWheel.setPower(PIDController.Calculate(targetState.FrontLeftEncoder, bot.state.FrontLeftEncoder));
            BackRightWheel.setPower(PIDController.Calculate(targetState.BackRightEncoder, bot.state.BackRightEncoder));
            BackLeftWheel.setPower(PIDController.Calculate(targetState.BackLeftEncoder, bot.state.BackLeftEncoder));
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
}
