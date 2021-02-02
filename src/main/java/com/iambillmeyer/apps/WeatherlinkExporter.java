package com.iambillmeyer.apps;

import com.iambillmeyer.model.AppOptions;
import com.iambillmeyer.weatherlink.Loop2Packet;
import com.iambillmeyer.weatherlink.WeatherLinkIP;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WeatherlinkExporter
{
    WeatherLinkIP weatherLinkIP;
    HTTPServer s;
    Gauge barometer;
    Gauge dailyEt;
    Gauge dailyRain;
    Gauge dewPoint;
    Gauge heatIndex;
    Gauge insideHumidity;
    Gauge insideTemperature;
    Gauge last15MinRain;
    Gauge last24HourRain;
    Gauge lastHourRain;
    Gauge outsideHumidity;
    Gauge outsideTemperature;
    Gauge rainRate;
    Gauge solarRadiation;
    Gauge startDateCurrentStorm;
    Gauge stormRain;
    Gauge tenMinAvgWindSpeed;
    Gauge tenMinWindGust;
    Gauge tenMinWindGustDirection;
    Gauge thswIndex;
    Gauge twoMinAvgWindSpeed;
    Gauge uv;
    Gauge windDirection;
    Gauge windSpeed;

    AppOptions appOptions;
    RandomAccessFile demoFile;

    public WeatherlinkExporter(AppOptions appOptions)
    throws java.io.IOException
    {
        this.appOptions = appOptions;
        weatherLinkIP = new WeatherLinkIP();
        initMetrics();

        if (appOptions.demoDataFilename != null)
        {
            demoFile = new RandomAccessFile(appOptions.demoDataFilename, "r");
        }
    }

    private void initMetrics()
    throws java.io.IOException
    {
        final String ns = "weatherlink";

        // @formatter:off
        barometer = Gauge
                .build("barometer", "Current Barometer. Units are (in Hg / 1000). The barometric value should be between 20 inches and 32.5 inches. Values outside these ranges will not be logged.")
                .namespace(ns)
                .register();

        dailyEt = Gauge
                .build("dailyEt", "This value is sent as the 1000th of an inch.")
                .namespace(ns)
                .register();

        dailyRain = Gauge
                .build("dailyRain", "This value is sent as number of rain clicks. (0.2mm or 0.01in)")
                .namespace(ns)
                .register();

        dewPoint = Gauge
                .build("dewPoint", "The value is a signed two byte value in whole degrees F. 255 = dashed data")
                .namespace(ns)
                .register();

        heatIndex = Gauge
                .build("heatIndex", "The value is a signed two byte value in whole degrees F. 255 = dashed data")
                .namespace(ns)
                .register();

        insideHumidity = Gauge.build("insideHumidity", "This is the relative humidity in %, such as 50 is returned for 50%.")
                .namespace(ns)
                .register();

        insideTemperature = Gauge.build("insideTemperature", "The value is sent as 10th of a degree in F. For example, 795 is returned for 79.5°F.")
                .namespace(ns)
                .register();

        last15MinRain = Gauge.build("last15MinRain", "This value is sent as number of rain clicks. (0.2mm or 0.01in)")
                .namespace(ns)
                .register();

        last24HourRain = Gauge.build("last24HourRain", "This value is sent as number of rain clicks. (0.2mm or 0.01in)")
                .namespace(ns)
                .register();

        lastHourRain = Gauge.build("lastHourRain", "This value is sent as number of rain clicks. (0.2mm or 0.01in)")
                .namespace(ns)
                .register();

        outsideHumidity = Gauge.build("outsideHumidity", "This is the relative humidity in %, such as 50 is returned for 50%.")
                .namespace(ns)
                .register();

        outsideTemperature = Gauge.build("outsideTemperature", "The value is sent as 10th of a degree in F. For example, 795 is returned for 79.5°F.")
                .namespace(ns)
                .register();

        rainRate = Gauge.build("rainRate", "In rain clicks per hour.")
                .namespace(ns)
                .register();

        solarRadiation = Gauge.build("solarRadiation", "The unit is in watt/meter^2.")
                .namespace(ns)
                .register();

        startDateCurrentStorm = Gauge.build("startDateCurrentStorm", "Bit 15 to bit 12 is the month, bit 11 to bit 7 is the day and bit 6 to bit 0 is the year offseted by 2000.")
                .namespace(ns)
                .register();

        stormRain = Gauge.build("stormRain", "The storm is stored as number of rain clicks. (0.2mm or 0.01in)")
                .namespace(ns)
                .register();

        tenMinAvgWindSpeed = Gauge.build("tenMinAvgWindSpeed", "It is a two-byte unsigned value in 0.1mph resolution.")
                .namespace(ns)
                .register();

        tenMinWindGust = Gauge.build("tenMinWindGust", "It is a two-byte unsigned value in 0.1mph resolution.")
                .namespace(ns)
                .register();

        tenMinWindGustDirection = Gauge.build("tenMinWindGustDirection", "It is a two-byte unsigned value from 1 to 360 degrees. (0° is no wind data, 90° is East, 180° is South, 270° is West and 360° is north)")
                .namespace(ns)
                .register();

        thswIndex = Gauge.build("thswIndex", "The value is a signed two byte value in whole degrees F. 255 = dashed data")
                .namespace(ns)
                .register();

        twoMinAvgWindSpeed = Gauge.build("twoMinAvgWindSpeed", "It is a two-byte unsigned value in 0.1mph resolution.")
                .namespace(ns)
                .register();

        uv = Gauge.build("uv", "Unit is in UV Index")
                .namespace(ns)
                .register();

        windDirection = Gauge.build("windDirection", "It is a two-byte unsigned value from 1 to 360 degrees. (0° is no wind data, 90° is East, 180° is South, 270° is West and 360° is north)")
                .namespace(ns)
                .register();

        windSpeed = Gauge.build("windSpeed", "It is a byte unsigned value in mph. If the wind speed is dashed because it lost synchronization with the radio or due to some other reason, the wind speed is forced to be 0.")
                .namespace(ns)
                .register();
        // @formatter:on

        s = new HTTPServer(9321);
    }

    public void run()
    {
        boolean success;
        Loop2Packet packet = new Loop2Packet();

        while (true)
        {
            System.out.println("==================================");
            System.out.println("Taking reading at " + new java.util.Date());
            long start = System.currentTimeMillis();

            success = readWeatherData(packet);
            if (success == true)
            {
                barometer.set(packet.barometer);
                dailyEt.set(packet.dailyEt);
                dailyRain.set(packet.dailyRain);
                dewPoint.set(packet.dewPoint);
                heatIndex.set(packet.heatIndex);
                insideHumidity.set(packet.insideHumidity);
                insideTemperature.set(packet.insideTemperature);
                last15MinRain.set(packet.last15MinRain);
                last24HourRain.set(packet.last24HourRain);
                lastHourRain.set(packet.lastHourRain);
                outsideHumidity.set(packet.outsideHumidity);
                outsideTemperature.set(packet.outsideTemperature);
                rainRate.set(packet.rainRate);
                solarRadiation.set(packet.solarRadiation);
                startDateCurrentStorm.set(packet.startDateCurrentStorm);
                stormRain.set(packet.stormRain);
                tenMinAvgWindSpeed.set(packet.tenMinAvgWindSpeed);
                tenMinWindGust.set(packet.tenMinWindGust);
                tenMinWindGustDirection.set(packet.tenMinWindGustDirection);
                thswIndex.set(packet.thswIndex);
                twoMinAvgWindSpeed.set(packet.twoMinAvgWindSpeed);
                uv.set(packet.uv);
                windDirection.set(packet.windDirection);
                windSpeed.set(packet.windSpeed);

                System.out.printf("Outside temperature is %.2f\n", packet.outsideTemperature);
            }

            long stop = System.currentTimeMillis();
            final int delay = 60;
            System.out.printf("Completed in %d msecs.  Sleeping %d seconds.\n", (stop - start), delay);
            try
            {
                Thread.sleep(delay * 1000L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean readWeatherData(Loop2Packet packet)
    {
        boolean success;

        if (demoFile != null)
        {
            success = readFromDemoData(packet);
        }
        else
        {
            success = readFromWeatherStation(packet);
        }

        return success;
    }

    private boolean readFromDemoData(Loop2Packet packet)
    {
        byte[] buf = new byte[99];
        boolean success = false;

        try
        {
            // Wrap back to the start of the file if we're at the end...
            if (demoFile.getFilePointer() >= demoFile.length())
            {
                demoFile.seek(0L);
            }

            demoFile.read(buf);
            packet.parse(buf);
            success = true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return success;
    }

    private boolean readFromWeatherStation(Loop2Packet packet)
    {
        boolean success = false;

        try
        {
            System.out.printf("Connecting...\n");
            System.out.flush();
            weatherLinkIP.connect(appOptions.host, appOptions.port);

            System.out.printf("Sending...\n");
            System.out.flush();
            success = weatherLinkIP.sendLPSCmd(packet);

            System.out.printf("Disconnecting...\n");
            System.out.flush();
            weatherLinkIP.disconnect();

            System.out.printf("Complete.\n");
            System.out.flush();
        }
        catch (java.io.IOException e)
        {
            System.err.printf("Failed to connect to WeatherLink: %s\n", e.getMessage());

            try
            {
                weatherLinkIP.disconnect();
            }
            catch (java.io.IOException ignored)
            {
            }
        }

        return success;
    }
}
