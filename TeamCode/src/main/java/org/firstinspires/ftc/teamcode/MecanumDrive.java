package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class MecanumDrive
{
    DcMotorEx FrontRightWheel, FrontLeftWheel, BackRightWheel, BackLeftWheel;

    public MecanumDrive(DcMotorEx FrontRightMotor, DcMotorEx FrontLeftMotor, DcMotorEx BackRightMotor, DcMotorEx BackLeftMotor)
    {
        this.FrontRightWheel = FrontRightMotor;
        this.FrontLeftWheel = FrontLeftMotor;
        this.BackRightWheel = BackRightMotor;
        this.BackLeftWheel = BackLeftMotor;

    }

    public void Turn(double angle, double power)
    {
        if (angle > 0){
            FrontLeftWheel.setPower(power);
            FrontRightWheel.setPower(-power);
            BackLeftWheel.setPower(power);
            BackRightWheel.setPower(-power);
        }
        else {
            FrontLeftWheel.setPower(-power);
            FrontRightWheel.setPower(power);
            BackLeftWheel.setPower(-power);
            BackRightWheel.setPower(power);
        }

    }

    public void MoveAuto(double angle, double yaw, double power)
    {


        // Changes degrees to radians
        angle *= Math.PI/180;

        Move(new Vector2D(Math.cos(angle), Math.sin(angle)), yaw, power);
    }

    public void Move(Vector2D direction, double yaw, double power)
    {
        double max;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double frontLeftPower  = direction.y + direction.x + yaw;
        double frontRightPower = direction.y - direction.x - yaw;
        double backLeftPower   = direction.y - direction.x + yaw;
        double backRightPower  = direction.y + direction.x - yaw;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower  /= max;
            frontRightPower /= max;
            backLeftPower   /= max;
            backRightPower  /= max;
        }

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
