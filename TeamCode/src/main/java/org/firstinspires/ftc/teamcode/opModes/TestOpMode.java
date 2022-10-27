package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Drivetrain;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.SwerveDrive;
import org.firstinspires.ftc.teamcode.SwerveModule;
import org.firstinspires.ftc.teamcode.UltrasonicSensor;
import org.firstinspires.ftc.teamcode.Vector2D;

@TeleOp(name = "Testbench")
public class TestOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(hardwareMap.get(DigitalChannel.class, "US_Trigger"), hardwareMap.get(DigitalChannel.class, "US_Echo"));

        waitForStart();

        while(opModeIsActive())
        {
            telemetry.addData("Bruv","Moment");
            telemetry.addData("Distance? ", ultrasonicSensor.ping());
            telemetry.addData("Delay:", ultrasonicSensor.delay);
            telemetry.addData("Intial:", ultrasonicSensor.time_initial);
            telemetry.addData("Final", ultrasonicSensor.time_final);
            telemetry.update();
        }

    }
}
