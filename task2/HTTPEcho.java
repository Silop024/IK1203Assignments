import java.net.*;
import java.io.*;

public class HTTPEcho
{
    static int BUFFERSIZE = 1024;

    public static void main(String[] args) throws Exception
    {
        int port = 0;

        try
        { port = Integer.parseInt(args[0]); }
        catch(Exception ex)
        {
            System.err.println("Usage: TCPEcho port");
            System.exit(1);
        }
        //Open the door on the given port
        ServerSocket welcomeSocket = new ServerSocket(port);

        while(true)
        {
            //Establish the TCP connection with the client
            Socket connectionSocket = welcomeSocket.accept();
            connectionSocket.setSoTimeout(60000);


            //Buffer for client requests
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
            //Length of the request as in how many bytes?
            int fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer);



            //byte[] toClientBuffer =
            connectionSocket.getOutputStream().write(fromClientBuffer);
            connectionSocket.close();
        }
    }
}
