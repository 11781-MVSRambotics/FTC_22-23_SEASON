package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.utils.PIDController;

// This class contains all the code required for cone manipulation
// Controls rotation of the turntable, extension of slides
// Also controls arm and claw for cone interaction
public class Turret {

    public State currentState;
    public State targetState;

    // Instance objects for hardware motors and servos
    DcMotorEx TurnMotor, ExtendMotor;
    Servo RightArmServo, LeftArmServo, RightClawServo, LeftClawServo;

    DigitalChannel SlideLimiter;
    // Enumerations to specify the two types of movement used by motion methods
    // ABSOLUTE moves to a given angle/encoder value regardless of current position
    // RELATIVE moves a specified angle from its current position
    public enum ExtendMode
    {
        ABSOLUTE,
        RELATIVE
    }
    public enum RotateMode
    {
        ABSOLUTE,
        RELATIVE
    }

    static class State
    {
        // Motor
        public int TurnTablePosition;
        public int SlidesPosition;

        // Servo
        public double ArmPosition;
        public double ClawPosition;
    }

    // Turret constructor that takes references to two motors and two servos
    // Also responsible for configuring basic motor behaviors
    public Turret(DcMotorEx TurnMotor, DcMotorEx ExtendMotor, Servo LeftArmServo, Servo RightArmServo, Servo RightClawServo, Servo LeftClawServo, DigitalChannel SlideLimiter)
    {
        // Reverse the motor direction to make it more intuitive
        TurnMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Configure motors to break when not under load
        TurnMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ExtendMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Zero out the motor positions and configure them to the correct mode
        TurnMotor.setTargetPosition(0);
        ExtendMotor.setTargetPosition(0);
        TurnMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        ExtendMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Link parameter references to instance variables
        this.TurnMotor = TurnMotor;
        this.ExtendMotor = ExtendMotor;
        this.RightArmServo = RightArmServo;
        this.LeftArmServo = LeftArmServo;
        this.RightClawServo = RightClawServo;
        this.LeftClawServo = LeftClawServo;
        this.SlideLimiter = SlideLimiter;
    }

    public void Move()
    {
        UpdateCurrentState();
        if (!TargetStateReached())
        {
            PIDController pid = new PIDController(1, 1, 1);

            if (!SlideLimiter.getState())
            {
                ExtendMotor.setTargetPosition(targetState.SlidesPosition);
            }

            TurnMotor.setTargetPosition(targetState.TurnTablePosition);
            RightArmServo.setPosition(targetState.ArmPosition);
            LeftArmServo.setPosition(targetState.ArmPosition);
            RightClawServo.setPosition(targetState.ClawPosition);
            LeftClawServo.setPosition(targetState.ClawPosition);

        }

        TurnMotor.setPower(0.2);
    }

    // Method for rotating the turntable with two different modes
    // Can turn in reference to current location or to an absolute position
    public void AddRotationInput(int degrees, RotateMode mode)
    {
        //Convert degree value to encoder ticks
        //1120 encoder ticks per revolution of the output shaft
        //5:1 gear ratio
        int encoder_value = degrees * (1120 / 360) * 5;

        // Check the movement mode and set the value accordingly
        if (mode == RotateMode.ABSOLUTE)
        {
            targetState.TurnTablePosition = encoder_value;
        }
        else if (mode == RotateMode.RELATIVE)
        {
            targetState.TurnTablePosition = currentState.TurnTablePosition + encoder_value;
        }
    }

    //Extending the linear slides by a length in centimeters and a speed in encoder ticks per second
    public void AddExtensionInput(double length, ExtendMode mode)
    {
        //Convert centimeters to encoder ticks
        //1120 encoder ticks per revolution of the output shaft
        int encoder_value =  (int) ((length / 5.02654824574) * 1120);

        // Check extension mode and set value accordingly
        if (mode == ExtendMode.ABSOLUTE)
        {
            targetState.SlidesPosition = encoder_value;
        }
        else if (mode == ExtendMode.RELATIVE)
        {
            targetState.SlidesPosition = currentState.SlidesPosition + encoder_value;
        }
    }

    boolean TargetStateReached()
    {
        return
                Math.abs(targetState.TurnTablePosition - currentState.TurnTablePosition) < 10
                &&
                Math.abs(targetState.SlidesPosition - currentState.SlidesPosition) < 10
                &&
                Math.abs(targetState.ArmPosition - currentState.ArmPosition) < 10
                &&
                Math.abs(targetState.ClawPosition - currentState.ClawPosition) < 10;
    }

    void UpdateCurrentState()
    {
        currentState.TurnTablePosition = TurnMotor.getCurrentPosition();
        currentState.SlidesPosition = ExtendMotor.getCurrentPosition();
        currentState.ArmPosition = (RightArmServo.getPosition() + LeftArmServo.getPosition()) / 2;
        currentState.ClawPosition = (RightClawServo.getPosition() + LeftClawServo.getPosition()) / 2;
    }
}
