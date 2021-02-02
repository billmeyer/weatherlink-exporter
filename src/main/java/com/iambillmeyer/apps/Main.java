package com.iambillmeyer.apps;

import com.iambillmeyer.model.AppOptions;
import org.apache.commons.cli.ParseException;

public class Main
{
    public static void main(String[] args)
    {
        WeatherlinkExporter app = null;
        AppOptions appOptions = new AppOptions();

        try
        {
            appOptions.loadArgs(args);
        }
        catch (ParseException e)
        {
            appOptions.showUsage();
            return;
        }

        try
        {
            app = new WeatherlinkExporter(appOptions);
        }
        catch (java.io.IOException e)
        {
            System.err.printf("Failed to initialize Prometheus: %s\n", e.getMessage());
            System.exit(-1);
        }

        app.run();
    }
}
