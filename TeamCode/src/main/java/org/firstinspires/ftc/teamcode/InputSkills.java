package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class InputSkills extends Bot{

    public void Move(double axial, double lateral, double yaw)
    {
        double max;

        // Forward returns negative value
        axial = -axial;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double frontLeftPower  = axial + lateral + yaw;
        double frontRightPower = axial - lateral - yaw;
        double backLeftPower   = axial - lateral + yaw;
        double backRightPower  = axial + lateral - yaw;

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

        frontLeftPower  *= Bot.chassis_speed_cap;
        frontRightPower *= Bot.chassis_speed_cap;
        backLeftPower   *= Bot.chassis_speed_cap;
        backRightPower  *= Bot.chassis_speed_cap;

        // Send calculated power to wheels
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backLeftMotor.setPower(backLeftPower);
        backRightMotor.setPower(backRightPower);
    }

    public void ChangeArmPosition(int positionChange, double power)
    {
        int target = armMotor.getCurrentPosition() + positionChange;

        // Checking if the requested movement is within the arms range of motion
        if (target > 1700 || target < 0)
        {
            armMotor.setTargetPosition(armMotor.getCurrentPosition());
            armMotor.setPower(arm_speed_cap);
            return;
        }

        power = power * Bot.arm_speed_cap;

        armMotor.setTargetPosition(target);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armMotor.setPower(power);
    }

    public void ChangeClawPosition(double positionChange)
    {
        if (clawServo.getPosition() + positionChange > 1) {clawServo.setPosition(1);}
        else if (clawServo.getPosition() + positionChange < 0) {clawServo.setPosition(0);}
        else {clawServo.setPosition(clawServo.getPosition() + positionChange);}
    }

    public void ChangeWristOffset(double positionChange)
    {
        if (wristServo.getPosition() + positionChange > 1) {wristServo.setPosition(1);}
        else if (wristServo.getPosition() + positionChange < 0) {wristServo.setPosition(0);}
        else {wristServo.setPosition(wristServo.getPosition() + positionChange);}
    }

    public void ToggleSpin (float power)
    {
        if (spinMotor.getPower() != 0) {spinMotor.setPower(0);}
        else {spinMotor.setPower(power * spinner_speed_cap);}
    }

    public void SetMaxSpeed (double chassis_speed, double arm_speed, double spinner_speed)
    {
        Bot.chassis_speed_cap = chassis_speed;
        Bot.arm_speed_cap = arm_speed;
        Bot.spinner_speed_cap = spinner_speed;
    }
}