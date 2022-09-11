package org.firstinspires.ftc.teamcode;

import android.content.res.Resources;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;;

import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;

import java.io.*;
import java.io.FileInputStream;

public class RobotSpeakerImpl implements RobotSpeaker {

    private static byte[] fileToByteArray(String name) throws FileNotFoundException
    {
        byte[] decoded = new byte[0];

        try {
            InputStream input = new FileInputStream(name);
            System.out.println("Available bytes in the file: " + input.available());

            decoded = new byte[input.available()];

            // Read byte from the input stream
            input.read(decoded);
            System.out.println("Data read from the file: ");

            // Convert byte array into string
            String data = new String(decoded);
            System.out.println(data);

            // Close the input stream
            input.close();
        } catch (Exception e) {
            e.getStackTrace();
        }

        return decoded;
    }

    private static void portBytesOut(byte[] bytes)
    {
        AnalogOutput speakerPort = new AnalogOutput(Bot.analogController1, 1);

        for (byte b : bytes)
        {
            speakerPort.setAnalogOutputVoltage(b);
        }
    }

    // Hardware device methods
    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {}

    @Override
    public void close() {}
}
