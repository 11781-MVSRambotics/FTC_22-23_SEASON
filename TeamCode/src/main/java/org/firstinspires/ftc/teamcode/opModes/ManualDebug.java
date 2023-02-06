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

        CRServo LeftArmServo = hardwareMap.get(CRServo.class, "LeftArmServo");
        CRServo RightArmServo = hardwareMap.get(CRServo.class, "RightArmServo");


        waitForStart();

        while (opModeIsActive())
        {
            if (gamepad1.dpad_up)
            {
                RightArmServo.setPower(-1);
                LeftArmServo.setPower(1);
            }
            else if (gamepad1.dpad_down)
            {
                RightArmServo.setPower(1);
                LeftArmServo.setPower(-1);
            }
            else
            {
                RightArmServo.setPower(0);
                LeftArmServo.setPower(0);
            }

            telemetry.addData("LeftArm Power: ", LeftArmServo.getPower());
            telemetry.addData("RightArm Power: ", RightArmServo.getPower());

            telemetry.update();
        }
    }
}
