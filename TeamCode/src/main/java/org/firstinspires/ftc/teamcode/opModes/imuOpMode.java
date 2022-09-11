package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.SensorSkills;

@Autonomous(name = "imuOpMode", group = "OpModes")
public class imuOpMode extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        Bot.initializeHWMap(hardwareMap);
        SensorSkills sensors = new SensorSkills();

        waitForStart();

        while (opModeIsActive())
        {
            sensors.UpdateIMUData(AxesReference.EXTRINSIC, AxesOrder.XYZ);
            sensors.CameraNavigation(25);

            telemetry.addData("Acceleration Data: ", Bot.acceleration);
            telemetry.addData("Orientation Data: ", Bot.orientation);
            telemetry.update();
        }
    }
}
