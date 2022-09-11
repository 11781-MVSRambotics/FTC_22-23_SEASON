package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.InputSkills;

@TeleOp(name = "New Driver", group = "OpModes")
public class TestOpMode extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    // Logic Flags
    private boolean spinner_flag = true;

    @Override
    public void runOpMode() {

        Bot.initializeHWMap(hardwareMap);

        // Instantiate the class containing our driver-controlled methods
        InputSkills skills = new InputSkills();

        // Waits for the user to hit start. Anything above this happens during initialization
        waitForStart();

        // Main loop runs as long as the opMode remains active (started and not stopped)
        while (opModeIsActive())
        {
            // Omni-capable driving and tank turning
            skills.Move(this.gamepad1.left_stick_y, this.gamepad1.left_stick_x, this.gamepad1.right_stick_x);

            // Logic to govern the direction of the movement for the arm (up, down, and halt respectively)
            // Adjusting trigger pressure slows the movement. Lowering positionChange does the same to a degree
            if (this.gamepad1.right_trigger > 0) {
                skills.ChangeArmPosition(100, this.gamepad1.right_trigger);
            } else if (this.gamepad1.left_trigger > 0) {
                skills.ChangeArmPosition(-100, this.gamepad1.left_trigger);
            } else {
                skills.ChangeArmPosition(0, 0.5);
            }

            // Logic to govern claw movement. All possible movements are mapped to the D-pad
            // Left and right are open and close
            // Up and down are up/down rotation of the wrist
            if (this.gamepad1.dpad_left || this.gamepad1.dpad_right) {
                if (this.gamepad1.dpad_left) {
                    skills.ChangeClawPosition(0.01);
                } else {
                    skills.ChangeClawPosition(-0.01);
                }
            } else {
                if (this.gamepad1.dpad_up) {
                    skills.ChangeWristOffset(-0.01);
                } else if (this.gamepad1.dpad_down) {
                    skills.ChangeWristOffset(0.01);
                }
            }

            // Flag logic for toggling the duck-spinner (probably an easier way to do this)
            if (this.gamepad1.a && spinner_flag)
            {
                skills.ToggleSpin(1);
                spinner_flag = false;
            }
            else if (!this.gamepad1.a && !spinner_flag) {spinner_flag = true;}

            // Enable CHAOS MODE
            if (this.gamepad1.y) {skills.SetMaxSpeed(1, 1, 1);}
            else {skills.SetMaxSpeed(0.5, 0.5, 0.5);}

            // Debugging print-outs go here, update happens last
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
