package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Bot;

public class CompAuto extends LinearOpMode {

    Bot bot = new Bot(hardwareMap);

    @Override
    public void runOpMode()
    {
        waitForStart();

        bot.UpdateState();
    }
}
