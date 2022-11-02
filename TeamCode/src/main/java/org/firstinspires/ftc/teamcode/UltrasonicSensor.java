package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;


public class UltrasonicSensor
{
    DigitalChannel trigger;
    DigitalChannel echo;

    public long time_initial = 0;
    public long time_final = 0;
    public double delay = 0;

    public UltrasonicSensor(DigitalChannel in, DigitalChannel out)
    {
        in.setMode(DigitalChannel.Mode.INPUT);
        out.setMode(DigitalChannel.Mode.OUTPUT);
        in.setState(true);
        out.setState(true);

        trigger = in;
        echo = out;
    }

    public double ping()
    {
        time_initial = 0;
        time_final = 0;
        delay = 0;

        trigger.setState(false);
        try {Thread.sleep(0, 10000);}
        catch (InterruptedException e) {e.printStackTrace();}
        trigger.setState(true);
        time_initial = System.nanoTime();
        while (echo.getState())
        {
            time_final = System.nanoTime();
        }

        delay = time_final - time_initial;

        return (delay / 1000000000) * 344;
    }


}
