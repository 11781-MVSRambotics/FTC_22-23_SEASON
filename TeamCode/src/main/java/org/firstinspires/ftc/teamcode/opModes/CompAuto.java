package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.MecanumDrive;

@SuppressWarnings("unused")
@Autonomous(name = "CompAutonomous")
public class CompAuto extends LinearOpMode {

    @Override
    public void runOpMode()
    {
        Bot bot = new Bot(hardwareMap);

        bot.chassis.FrontRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.chassis.FrontLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.chassis.BackRightWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bot.chassis.BackLeftWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        bot.chassis.DriveAutoBad(7, 0.3);


    }
}
