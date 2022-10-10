package org.firstinspires.ftc.teamcode;

import java.util.Vector;

public class SwerveDrive extends Drivetrain
{
    public SwerveModule RightModule, LeftModule;

    public SwerveDrive (SwerveModule RightModule, SwerveModule LeftModule)
    {
        this.RightModule = RightModule;
        this.LeftModule = LeftModule;
    }

    public void Move(Vector2D direction, double yaw)
    {
        Vector2D leftTurn = new Vector2D(0, yaw);
        Vector2D rightTurn = new Vector2D(0, -yaw);

        Vector2D leftFinal = Vector2D.add(leftTurn, direction);
        Vector2D rightFinal = Vector2D.add(rightTurn, direction);

        RightModule.Move(rightFinal);
        LeftModule.Move(leftFinal);

    }

}
