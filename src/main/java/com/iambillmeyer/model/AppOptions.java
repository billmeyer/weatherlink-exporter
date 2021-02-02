package com.iambillmeyer.model;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AppOptions
{
    Options options;
    public String host;
    public int port;
    public String demoDataFilename;

    public AppOptions()
    {
        options = new Options();
        options.addOption("h", "host", true, "IP Address of the WeatherLink weather station.");
        options.addOption("p", "port", true, "Port number the weather station is listening for connections on.");
        options.addOption("d", "demodata", true, "Path to a file containing sample weather data to be used instead of an actual weather station.");
    }

    public void loadArgs(String[] args)
    throws ParseException
    {
        if (args.length == 0)
            throw new ParseException("No arguments supplied");

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = parser.parse(options, args);

        // See if a demodata file is supplied...
        demoDataFilename = cl.getOptionValue("demodata");

        // otherwise, use the host and port arguments to talk to a weather station.
        if (demoDataFilename == null)
        {
            host = cl.getOptionValue("host");
            port = Integer.parseInt(cl.getOptionValue("port"));
        }
    }

    public void showUsage()
    {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("weatherlink-exporter", options);
    }
}
