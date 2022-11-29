package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Turret {

    DcMotorEx TurnMotor, ExtendMotor;
    Servo ArmServo, PickerServo;

    public Turret(DcMotorEx turnmotor, DcMotorEx extendmotor, Servo armservo, Servo pickerservo)
    {
        turnmotor = this.TurnMotor;
        turnmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        extendmotor = this.ExtendMotor;
        extendmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armservo = this.ArmServo;

        pickerservo = this.PickerServo;
    }

    public void Rotate(int degrees, double speed)
    {
        //Convert to encoder ticks
        int encoder_value = degrees * (1120 / 360);

        TurnMotor.setTargetPosition(encoder_value);
        TurnMotor.setVelocity(speed, AngleUnit.DEGREES);
    }

    public void Extend(double length, double speed)
    {

    }

    public void MoveArm(int degrees, double speed)
    {

    }
}
