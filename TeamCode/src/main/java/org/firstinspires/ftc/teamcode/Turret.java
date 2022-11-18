package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Turret {
    DcMotorEx TurnMotor;
    DcMotorEx ArmMotor;
    public BNO055IMU extimu;

    public Turret(DcMotorEx TurnMotor, DcMotorEx ArmMotor, BNO055IMU extimu)
    {
        this.TurnMotor = TurnMotor;
        this.ArmMotor = ArmMotor;
        this.extimu = extimu;
    }

    public void TurnTurret(double power)
    {
        TurnMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        TurnMotor.setPower(power);
    }

    public void TurnTurretAuto(int angle, double power)
    {

        TurnMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        TurnMotor.setTargetPosition(angle);
        TurnMotor.setPower(power);
    }

    public void ArmExtend(int position, double power)
    {
        ArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        if (position == 1)
        {
            ArmMotor.setTargetPosition(1 /* placeholder for top height/top pole */);
            ArmMotor.setPower(power);

        }
        else if (position == 2)
        {
            ArmMotor.setTargetPosition(1 /* placeholder for medium height/medium pole */);
            ArmMotor.setPower(power);

        }
        else if (position == 3)
        {
            ArmMotor.setTargetPosition(1 /* placeholder for low height/low pole */);
            ArmMotor.setPower(power);

        }
        else if (position == 4)
        {
            ArmMotor.setTargetPosition(1 /* placeholder for ground height/ground junction */);
            ArmMotor.setPower(power);

        }

    }
}
