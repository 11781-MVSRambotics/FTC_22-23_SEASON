package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.checkerframework.common.subtyping.qual.Bottom;

public class SwerveModule
{
    public DcMotorEx TopMotor, BottomMotor;

    private float TopEncoderValue, BottomEncoderValue;

    public SwerveModule(DcMotorEx TopMotor, DcMotorEx BottomMotor)
    {
        this.TopMotor = TopMotor;
        this.BottomMotor = BottomMotor;

        // 560 encoder ticks per revolution for 20:1 Hex HD Motors - 28 for motor shaft
        TopMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        BottomMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        TopEncoderValue = TopMotor.getCurrentPosition();
        BottomEncoderValue = BottomMotor.getCurrentPosition();
    }

    public void Move(Vector2D direction)
    {

    }
}
