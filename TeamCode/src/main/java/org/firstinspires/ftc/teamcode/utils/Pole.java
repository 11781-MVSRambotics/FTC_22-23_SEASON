package org.firstinspires.ftc.teamcode.utils;

import org.opencv.core.RotatedRect;

public class Pole extends RotatedRect
{
    enum Type
    {
        TALL,
        MEDIUM,
        SHORT
    }
    Type type;

    public static final double minArea = 500;

    public Pole()
    {
        double heightToWidthRatio = size.height / size.width;

        if (heightToWidthRatio > 100)
        {
            type = Type.TALL;
        }
        else if (heightToWidthRatio <= 99 && heightToWidthRatio >= 50)
        {
            type = Type.MEDIUM;
        }
        else if (heightToWidthRatio < 50)
        {
            type = Type.SHORT;
        }
    }

    public double getWidth()
    {
        return size.width;
    }

    public Type getType()
    {
        return type;
    }
}
