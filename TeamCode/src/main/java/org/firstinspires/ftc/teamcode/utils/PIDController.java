package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDController
{
    double Kp;
    double Ki;
    double Kd;

    double p = 0;
    double i = 0;
    double d = 0;

    public PIDController (double p, double i, double d)
    {
        this.Kp = p;
        this.Ki = i;
        this.Kd = d;
    }

    public double Calculate (double target, double actual)
    {
        double error = target - actual;

        ElapsedTime time = new ElapsedTime();

        double current_time;
        double previous_time = time.time();
        double current_error;
        double previous_error = 0;

        double max_i = 0;

        current_time = time.time();
        current_error = target - actual;

        p = Kp * current_error;

        i += Ki * (current_error * (current_time - previous_time));

        if (i > max_i)
        {
            i = max_i;
        }
        else if (i < -max_i)
        {
            i = -max_i;
        }

        d = Kd * (current_error - previous_error) / (current_time - previous_time);

        previous_error = current_error;
        previous_time = current_time;

        return p + i + d;
    }
}
