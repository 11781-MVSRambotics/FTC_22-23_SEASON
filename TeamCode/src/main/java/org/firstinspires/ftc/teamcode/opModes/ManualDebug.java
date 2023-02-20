package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Turret;

@SuppressWarnings("unused")
@TeleOp(name = "Debug OpMode")
public class ManualDebug extends LinearOpMode{

    @Override
    public void runOpMode()
    {

        Bot bot = new Bot(hardwareMap);

        waitForStart();

        while (opModeIsActive())
        {

            if (gamepad2.right_trigger > 0)
            {
                bot.turret.AddArmInput(gamepad2.right_trigger);
            }
            else if (gamepad2.left_trigger > 0)
            {
                bot.turret.AddArmInput(-gamepad2.left_trigger);
            }
            else
            {
                bot.turret.AddArmInput(0);
            }

            if (gamepad2.dpad_right)
            {
                bot.turret.AddClawInput(1);
            }
            else if (gamepad2.dpad_left)
            {
                bot.turret.AddClawInput(0);
            }

            bot.Move();
            bot.UpdateState();

            FtcDashboard.getInstance().sendTelemetryPacket(bot.turret.GetUpdatedTelemetry());

            telemetry.update();
        }
    }
}
