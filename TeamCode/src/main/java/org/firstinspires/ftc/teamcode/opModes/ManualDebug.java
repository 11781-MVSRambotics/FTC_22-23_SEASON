package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Bot;

@SuppressWarnings("unused")
@TeleOp(name = "Debug OpMode")
public class ManualDebug extends LinearOpMode{

    @Override
    public void runOpMode()
    {
        DcMotorEx FrontRightMotor = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        DcMotorEx FrontLeftMotor = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        DcMotorEx BackRightMotor = hardwareMap.get(DcMotorEx.class, "BackRightMotor");
        DcMotorEx BackLeftMotor = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");

        waitForStart();

        while (opModeIsActive())
        {

            FrontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            FrontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BackRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            BackLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            FrontRightMotor.setPower(1);
            FrontLeftMotor.setPower(1);
            BackRightMotor.setPower(1);
            BackLeftMotor.setPower(1);

            telemetry.addData("FrontRightMotor: ", FrontRightMotor.getCurrentPosition());
            telemetry.addData("FrontLeftMotor: ", FrontLeftMotor.getCurrentPosition());
            telemetry.addData("BackRightMotor: ", BackRightMotor.getCurrentPosition());
            telemetry.addData("BackLeftMotor: ", BackLeftMotor.getCurrentPosition());

            telemetry.update();
        }
    }
}
