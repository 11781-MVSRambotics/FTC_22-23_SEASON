package org.firstinspires.ftc.teamcode.utils;

// A simple class for creating and using 2D vectors
// This is because 3D vectors are scary
public class Vector2D
{
    // Values for the components of the the vector
    public double x, y;

    // Constructor that does basically nothing but it's good practice I guess
    public Vector2D (double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    // Add two objects of type Vector2D together
    public static Vector2D add(Vector2D first, Vector2D second)
    {
        // Return a new Vector2D with components equal to the sum of the argument's components
        return new Vector2D(first.x+second.x, first.y+second.y);
    }

    // Subtract one Vector2D object from another
    public static Vector2D subtract(Vector2D first, Vector2D second)
    {
        // Return a new Vector2D with components equal to the difference of the argument's components
        return new Vector2D(first.x-second.x, first.y-second.y);
    }
}
