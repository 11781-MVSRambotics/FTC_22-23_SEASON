package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;

@SuppressWarnings("unused")
@TeleOp(name = "Debug OpMode")
public class ManualDebug extends LinearOpMode{

    @Override
    public void runOpMode()
    {

        DigitalChannel SlideLimitSwitch = hardwareMap.get(DigitalChannel.class, "SlideLimiter");

        DcMotorEx turretLeft = hardwareMap.get(DcMotorEx.class, "ExtendMotorLeft");
        DcMotorEx turretRight = hardwareMap.get(DcMotorEx.class, "ExtendMotorRight");

        turretLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turretRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        turretLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        while (opModeIsActive())
        {
            if (gamepad1.right_trigger > 0)
            {
                turretLeft.setPower(gamepad1.right_trigger);
                turretRight.setPower(-gamepad1.right_trigger);
            }
            else if (gamepad1.left_trigger > 0)
            {
                turretLeft.setPower(-gamepad1.left_trigger);
                turretRight.setPower(gamepad1.left_trigger);
            }
            else
            {
                turretLeft.setPower(0);
                turretRight.setPower(0);
            }

            telemetry.addData("Limit Switch Value: ", SlideLimitSwitch.getState());
            telemetry.update();
        }
    }
}
