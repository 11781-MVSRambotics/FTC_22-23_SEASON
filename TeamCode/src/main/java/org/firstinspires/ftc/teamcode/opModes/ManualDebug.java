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
        Servo ClawServo = hardwareMap.get(Servo.class, "ClawServo");
        CRServo LeftArmServo = hardwareMap.get(CRServo.class, "LeftArmServo");
        CRServo RightArmServo = hardwareMap.get(CRServo.class, "RightArmServo");

        waitForStart();

        while (opModeIsActive())
        {
            LeftArmServo.setPower(1);
            RightArmServo.setPower(-1);

            telemetry.update();
        }
    }
}
