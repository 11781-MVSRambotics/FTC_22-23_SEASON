package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

// This class contains all the code required for cone manipulation
// Controls rotation of the turntable, extension of slides
// Also controls arm and claw for cone interaction
public class Turret {

    // Instance objects for hardware motors and servos
    public DcMotorEx TurnMotor, ExtendMotor;
    public Servo ArmServo, PickerServo;

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

    // Turret constructor that takes references to two motors and two servos
    // Also responsible for configuring basic motor behaviors
    public Turret(DcMotorEx TurnMotor, DcMotorEx ExtendMotor)
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
    }

    // Method for rotating the turntable with two different modes
    // Can turn in reference to current location or to an absolute position
    public void Rotate(int degrees, double speed, RotateMode mode)
    {
        //Convert degree value to encoder ticks
        //1120 encoder ticks per revolution of the output shaft
        //5:1 gear ratio
        int encoder_value = degrees * (1120 / 360) * 5;

        // Check the movement mode and set the value accordingly
        if (mode == RotateMode.ABSOLUTE)
        {
            TurnMotor.setTargetPosition(encoder_value);
        }
        else if (mode == RotateMode.RELATIVE)
        {
            TurnMotor.setTargetPosition(TurnMotor.getCurrentPosition() + encoder_value);
        }

        // Supply the motor with a power so it can move to the target
        TurnMotor.setVelocity(speed, AngleUnit.DEGREES);
    }

    //Extending the linear slides by a length in centimeters and a speed in encoder ticks per second
    public void Extend(double length, double speed, ExtendMode mode)
    {
        //Convert centimeters to encoder ticks
        //1120 encoder ticks per revolution of the output shaft
        int encoder_value =  (int) ((length / 5.02654824574) * 1120);

        // Check extension mode and set value accordingly
        if (mode == ExtendMode.ABSOLUTE)
        {
            ExtendMotor.setTargetPosition(encoder_value);
        }
        else if (mode == ExtendMode.RELATIVE)
        {
            ExtendMotor.setTargetPosition(ExtendMotor.getCurrentPosition() + encoder_value);
        }

        // Supply motor with a velocity so it can move
        ExtendMotor.setVelocity(speed);
    }
}
