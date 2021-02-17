package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class TCPClient
{
    private static int BUFFERSIZE = 10240;

    public static String askServer(String hostname, int port, String toServer) throws IOException
    {
        //Buffer for answer from server
        byte[] responseBuffer = new byte[BUFFERSIZE];

        //Encode our toServer string to bytes
        byte[] encodedToServer = toServer.getBytes(StandardCharsets.UTF_8);

        //Establish TCP connection with a timeout
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(10000);

        //Send request to server
        clientSocket.getOutputStream().write(encodedToServer, 0, encodedToServer.length);

        //Get server response
        //byte[] response = clientSocket.getInputStream().readAllBytes();
        //int responseLength = response.length;

        int responseLength = clientSocket.getInputStream().read(responseBuffer);
        //byte[] response = new byte[responseLength];
        //for(int i = 0; i < responseLength; i++)
        //    response[i] = responseBuffer[i];

        System.out.println(responseLength);

        //Decode our server response
        String decodedResponse = new String(responseBuffer, 0, responseLength, StandardCharsets.UTF_8);

        clientSocket.close();
        return decodedResponse;
    }

    public static String askServer(String hostname, int port) throws IOException
    {
        //Buffer for answer from server
        byte[] responseBuffer = new byte[BUFFERSIZE];

        //Establish TCP connection with a timeout
        Socket clientSocket = new Socket(hostname, port);
        clientSocket.setSoTimeout(10000);



        //Get server response
        //byte[] response = clientSocket.getInputStream().readAllBytes();
        //int responseLength = response.length;

        int responseLength = clientSocket.getInputStream().read(responseBuffer);
        //byte[] response = new byte[responseLength];
        //for(int i = 0; i < responseLength; i++)
        //    response[i] = responseBuffer[i];

        System.out.println(responseLength);


        //Decode our server response
        String decodedResponse = new String(responseBuffer, 0, responseLength,StandardCharsets.UTF_8);

        clientSocket.close();
        return decodedResponse;
    }
}
