package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Turret {

    public DcMotorEx TurnMotor, ExtendMotor;
    Servo ArmServo, PickerServo;

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

    public Turret(DcMotorEx turnmotor, DcMotorEx extendmotor)
    {
        turnmotor.setTargetPosition(0);
        turnmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        extendmotor.setTargetPosition(0);
        extendmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.TurnMotor = turnmotor;
        this.ExtendMotor = extendmotor;
    }

    public void Rotate(int degrees, double speed, RotateMode mode)
    {
        //Convert to encoder ticks
        //1120 encoder ticks per revolution of the output shaft
        int encoder_value = degrees * (1120 / 360);

        if (mode == RotateMode.ABSOLUTE)
        {
            TurnMotor.setTargetPosition(encoder_value);
        }
        else if (mode == RotateMode.RELATIVE)
        {
            TurnMotor.setTargetPosition(TurnMotor.getCurrentPosition() + encoder_value);
        }

        TurnMotor.setVelocity(speed, AngleUnit.DEGREES);
    }

    //Extending the linear slides by a length in centimeters and a speed in encoder ticks per second
    public void Extend(double length, double speed, ExtendMode mode)
    {
        //Convert to encoder ticks
        //1120 encoder ticks per revolution of the output shaft
        int encoder_value =  (int) ((length / 5.02654824574) * 1120);

        if (mode == ExtendMode.ABSOLUTE)
        {
            ExtendMotor.setTargetPosition(encoder_value);
        }
        else if (mode == ExtendMode.RELATIVE)
        {
            ExtendMotor.setTargetPosition(ExtendMotor.getCurrentPosition() + encoder_value);
        }


        ExtendMotor.setVelocity(speed);
    }
}
