package com.iambillmeyer.weatherlink;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WeatherLinkIP
{
    private InputStream in;
    private OutputStream out;
    private Socket socket;
    private FileOutputStream fileOut;

    private int ACK = 0x6;
    private int NACK = 0x21;
    private int CANCEL = 0x18;

    public WeatherLinkIP()
    {
        try
        {
            fileOut = new FileOutputStream("/tmp/sampledata.bin", true);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to establish a connection to the specified weather station.
     *
     * @param host The hostname or IP address of the weather station
     * @param port The port number the weather station is listening for connections on.
     * @throws IOException
     */
    public void connect(String host, int port)
    throws IOException
    {
        socket = new Socket(host, port);

        final int timeout = 10 * 1000;
        socket.setSoTimeout(timeout);
        System.out.printf("Socket timeout set to %d msecs.\n", timeout);

        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    public void disconnect()
    throws IOException
    {
        if (fileOut != null)
        {
            fileOut.flush();
        }

        if (in != null)
        {
            in.close();
            in = null;
        }

        if (out != null)
        {
            out.close();
            out = null;
        }

        if (socket != null)
        {
            socket.close();
            socket = null;
        }
    }

    public boolean sendLPSCmd(Loop2Packet response)
    {
        byte[] buf = new byte[99];
        response.zero();

        try
        {
            out.write("LPS 2 1\n".getBytes());
            out.flush();

            int rc = in.read();
            if (rc == ACK)
            {
                int count = in.read(buf);
                if ((buf[0] == 'L') && buf[1] == 'O' && buf[2] == 'O')
                {
                    fileOut.write(buf);
                    response.parse(buf);
//                    response.prettyPrint(System.out);
                }
            }
        }
        catch (java.net.SocketTimeoutException so)
        {
            System.err.printf("Timed out waiting for weather station to respond: %s\n", so.getMessage());
            return false;
        }
        catch (IOException e)
        {
            System.err.printf("Failed to read weather response: %s\n", e.getMessage());
            return false;
        }

        return true;
    }
}
