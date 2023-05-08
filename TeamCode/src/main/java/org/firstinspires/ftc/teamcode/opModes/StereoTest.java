package org.firstinspires.ftc.teamcode.opModes;

import android.content.Intent;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utils.DebugDisplay;

@TeleOp
public class StereoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException
    {
        hardwareMap.appContext.startActivity(new Intent(hardwareMap.appContext, DebugDisplay.class));
    }
}
