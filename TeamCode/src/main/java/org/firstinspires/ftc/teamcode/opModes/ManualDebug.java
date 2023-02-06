package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;

@SuppressWarnings("unused")
@TeleOp(name = "Debug OpMode")
public class ManualDebug extends LinearOpMode{

    @Override
    public void runOpMode()
    {

        DcMotorEx ExtendMotorLeft = hardwareMap.get(DcMotorEx.class, "ExtendMotorLeft");
        DcMotorEx ExtendMotorRight = hardwareMap.get(DcMotorEx.class, "ExtendMotorRight");

        waitForStart();

        while (opModeIsActive())
        {
            if (gamepad1.dpad_up)
            {
                ExtendMotorLeft.setPower(-1);
                ExtendMotorRight.setPower(1);
            }
            else if (gamepad1.dpad_down)
            {
                ExtendMotorLeft.setPower(1);
                ExtendMotorRight.setPower(-1);
            }
            else
            {
                ExtendMotorLeft.setPower(0);
                ExtendMotorRight.setPower(0);
            }
            telemetry.update();
        }
    }
}
