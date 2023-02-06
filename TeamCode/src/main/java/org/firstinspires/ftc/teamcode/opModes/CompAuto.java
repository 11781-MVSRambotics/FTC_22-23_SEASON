package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@SuppressWarnings("unused")
@Autonomous(name = "CompAutonomous")
public class CompAuto extends LinearOpMode {

    Bot bot = new Bot(hardwareMap);

    MecanumDrive chassis = bot.chassis;

    @Override
    public void runOpMode()
    {
        chassis.FrontRightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chassis.FrontLeftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chassis.BackRightWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chassis.BackLeftWheel.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        chassis.RotateAutoBasic(100, 0.5);
        chassis.DriveAutoBasic(100, 0.5);
    }
}
