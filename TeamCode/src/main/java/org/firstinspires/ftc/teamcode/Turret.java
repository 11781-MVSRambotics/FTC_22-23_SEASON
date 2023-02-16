package org.firstinspires.ftc.teamcode;

import android.transition.Slide;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.teamcode.utils.PIDController;

// This class contains all the code required for cone manipulation
// Controls rotation of the turntable, extension of slides
// Also controls arm and claw for cone interaction
public class Turret {

    public State currentState = new State();
    public State targetState = new State();

    // Instance objects for hardware motors and servos
    DcMotorEx TurnMotor, RightExtendMotor, LeftExtendMotor;
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
    public Turret(DcMotorEx TurnMotor, DcMotorEx RightExtendMotor, DcMotorEx LeftExtendMotor, Servo LeftArmServo, Servo RightArmServo, Servo RightClawServo, Servo LeftClawServo, DigitalChannel SlideLimiter)
    {
        TurnMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RightExtendMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LeftExtendMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Reverse the motor direction to make it more intuitive
        TurnMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        RightExtendMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        LeftExtendMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Configure motors to break when not under load
        TurnMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RightExtendMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftExtendMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Zero out the motor positions and configure them to the correct mode
        TurnMotor.setTargetPosition(0);
        RightExtendMotor.setTargetPosition(0);
        LeftExtendMotor.setTargetPosition(0);
        TurnMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RightExtendMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        LeftExtendMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Link parameter references to instance variables
        this.TurnMotor = TurnMotor;
        this.RightExtendMotor = RightExtendMotor;
        this.LeftExtendMotor = LeftExtendMotor;
        this.RightArmServo = RightArmServo;
        this.LeftArmServo = LeftArmServo;
        this.RightClawServo = RightClawServo;
        this.LeftClawServo = LeftClawServo;
        this.SlideLimiter = SlideLimiter;
    }

    public void Move()
    {
        TelemetryPacket turretTelemetry = new TelemetryPacket();
        turretTelemetry.addLine("TurnMotor distance to target: " + (targetState.TurnTablePosition - currentState.TurnTablePosition));
        turretTelemetry.addLine("ExtensionMotor distance to target: " + (targetState.SlidesPosition - currentState.SlidesPosition));
        FtcDashboard.getInstance().sendTelemetryPacket(turretTelemetry);

        UpdateCurrentState();
        if (!TargetStateReached())
        {
            if (SlideLimiter.getState())
            {
                RightExtendMotor.setTargetPosition(targetState.SlidesPosition);
                LeftExtendMotor.setTargetPosition(targetState.SlidesPosition);
            }

            TurnMotor.setTargetPosition(targetState.TurnTablePosition);
            RightArmServo.setPosition(targetState.ArmPosition);
            LeftArmServo.setPosition(targetState.ArmPosition);
            RightClawServo.setPosition(targetState.ClawPosition);
            LeftClawServo.setPosition(targetState.ClawPosition);

        }

        //RightExtendMotor.setPower(0.5);
        LeftExtendMotor.setPower(0.5);
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

    public void AddArmInput(double position)
    {
        RightArmServo.setPosition(position);
        LeftArmServo.setPosition(position);
    }

    public void AddClawInput(double position)
    {
        RightClawServo.setPosition(position);
        LeftClawServo.setPosition(position);
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
        currentState.SlidesPosition = (RightExtendMotor.getCurrentPosition() + LeftExtendMotor.getCurrentPosition()) / 2;
        currentState.ArmPosition = (RightArmServo.getPosition() + LeftArmServo.getPosition()) / 2;
        currentState.ClawPosition = (RightClawServo.getPosition() + LeftClawServo.getPosition()) / 2;
    }
}
