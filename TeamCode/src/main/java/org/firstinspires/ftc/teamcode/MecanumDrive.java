package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.utils.Vector2D;

// This class contains all the code required for traversing the field in both Auto and TeleOp
// An instance of this class is automatically created and initialized as a component of the Bot class
public class MecanumDrive
{
    // Instance variables referencing the chassis' motors
    public DcMotorEx FrontRightWheel, FrontLeftWheel, BackRightWheel, BackLeftWheel;

    //
    public MecanumDrive(DcMotorEx FrontRightMotor, DcMotorEx FrontLeftMotor, DcMotorEx BackRightMotor, DcMotorEx BackLeftMotor)
    {
        // Reversing the necessary motors so that the signs of power values match rotational direction
        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        this.FrontRightWheel = FrontRightMotor;
        this.FrontLeftWheel = FrontLeftMotor;
        this.BackRightWheel = BackRightMotor;
        this.BackLeftWheel = BackLeftMotor;
    }

    public void MoveAuto(double angle, double yaw, double power)
    {
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
