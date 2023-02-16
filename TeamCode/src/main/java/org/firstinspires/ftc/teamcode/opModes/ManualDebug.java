package org.firstinspires.ftc.teamcode.opModes;

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
            if (gamepad1.right_trigger > 0)
            {
                bot.turret.AddExtensionInput(30, Turret.ExtendMode.ABSOLUTE);
            }
            else if (gamepad1.left_trigger > 0)
            {
                bot.turret.AddExtensionInput(0, Turret.ExtendMode.ABSOLUTE);

            }
            else
            {
                bot.turret.AddExtensionInput(0, Turret.ExtendMode.RELATIVE);
            }


            bot.Move();

            telemetry.addData("Running yaaaaaa", "");
            telemetry.update();
        }
    }
}
