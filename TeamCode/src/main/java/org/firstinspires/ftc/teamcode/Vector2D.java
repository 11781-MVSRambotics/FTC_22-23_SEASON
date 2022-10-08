package org.firstinspires.ftc.teamcode;

public class Vector2D
{
    public double x;
    public double y;

    public Vector2D (double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public static Vector2D add(Vector2D first, Vector2D second)
    {
        Vector2D third = new Vector2D(0, 0);
        third.x = first.x + second.x;
        third.y = first.y + second.y;

        return third;
    }

    public static Vector2D subtract(Vector2D first, Vector2D second)
    {
        Vector2D third = new Vector2D(0, 0);
        third.x = first.x - second.x;
        third.y = first.y - second.y;

        return third;
    }
}
