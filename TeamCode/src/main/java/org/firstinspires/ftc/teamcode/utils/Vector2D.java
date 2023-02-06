package org.firstinspires.ftc.teamcode.utils;

// A simple class for creating and using 2D vectors
// This is because 3D vectors are scary
public class Vector2D
{
    // Values for the components of the the vector
    public double x, y, angle, magnitude;

    private Vector2D() {}

    // Constructor that does basically nothing but it's good practice I guess
    public static Vector2D ConstructFromComponents(double x, double y)
    {
        Vector2D v = new Vector2D();

        v.x = x;
        v.y = y;

        v.angle = Math.atan2(x, y);
        v.magnitude = Math.sqrt(x*x + y*y);

        return v;
    }

    public static Vector2D ConstructFromAngleAndMag(double angle, double magnitude)
    {
        Vector2D v = new Vector2D();

        v.angle = angle;
        v.magnitude = magnitude;

        v.x = magnitude * Math.cos(angle);
        v.y = magnitude * Math.sin(angle);

        return v;
    }

    // Add two objects of type Vector2D together
    public static Vector2D add(Vector2D first, Vector2D second)
    {
        // Return a new Vector2D with components equal to the sum of the argument's components
        return Vector2D.ConstructFromComponents(first.x+second.x, first.y+second.y);
    }

    // Subtract one Vector2D object from another
    public static Vector2D subtract(Vector2D first, Vector2D second)
    {
        // Return a new Vector2D with components equal to the difference of the argument's components
        return Vector2D.ConstructFromComponents(first.x-second.x, first.y-second.y);
    }

    public static Vector2D rotate(Vector2D input, double angle)
    {
        return Vector2D.ConstructFromAngleAndMag(input.angle + angle, input.magnitude);
    }
}
