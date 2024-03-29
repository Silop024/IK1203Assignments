package tcpclient;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient
{
    private static final int BUFFERSIZE = 1024;

    public static String askServer(String hostname, int port, String toServer) throws IOException
    {
        //Buffer for answer from server
        byte[] responseBuffer = new byte[BUFFERSIZE];

        //Encode our toServer string to bytes
        byte[] encodedToServer = toServer.getBytes(StandardCharsets.UTF_8);

        //Establish TCP connection with a timeout
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(5000);

        //Send request to server
        clientSocket.getOutputStream().write(encodedToServer, 0, encodedToServer.length);

        //Get server response
        StringBuilder sb = new StringBuilder();
        int max = 10;
        int count = 0;
        InputStream inputStream = clientSocket.getInputStream();

        int responseLength = inputStream.read(responseBuffer, 0, 1024);
        if (responseLength >= 1024) {
            while (responseLength != -1 && count < max) {
                sb.append(new String(responseBuffer, 0, responseLength, StandardCharsets.UTF_8));
                responseLength = inputStream.read(responseBuffer, 0, 1024);
                count++;
            }
        } else {
            sb.append(new String(responseBuffer, 0, responseLength, StandardCharsets.UTF_8));
        }

        clientSocket.close();
        return sb.toString();
    }

    public static String askServer(String hostname, int port) throws IOException
    {
        //Buffer for answer from server
        byte[] responseBuffer = new byte[BUFFERSIZE];

        //Establish TCP connection with a timeout
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(5000);


        //Get server response
        StringBuilder sb = new StringBuilder();
        int max = 10;
        int count = 0;
        InputStream inputStream = clientSocket.getInputStream();

        int responseLength = inputStream.read(responseBuffer, 0, 1024);
        if (responseLength >= 1024) {
            while (responseLength != -1 && count < max) {
                sb.append(new String(responseBuffer, 0, responseLength, StandardCharsets.UTF_8));
                responseLength = inputStream.read(responseBuffer, 0, 1024);
                count++;
            }
        } else {
            sb.append(new String(responseBuffer, 0, responseLength, StandardCharsets.UTF_8));
        }

        clientSocket.close();
        return sb.toString();
    }
}
