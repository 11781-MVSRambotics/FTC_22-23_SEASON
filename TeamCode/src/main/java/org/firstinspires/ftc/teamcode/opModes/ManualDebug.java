package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Bot;

@SuppressWarnings("unused")
@TeleOp(name = "CompTeleOp")
public class ManualDebug extends LinearOpMode{

    @Override
    public void runOpMode()
    {
        Bot bot = new Bot(hardwareMap);

        waitForStart();

        while (opModeIsActive())
        {
            telemetry.update();
        }
    }
}
