import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class HTTPEcho
{
    static int BUFFERSIZE = 1024;

    public static void main(String[] args) throws Exception
    {
        Socket connectionSocket;
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
            connectionSocket = welcomeSocket.accept();
            connectionSocket.setSoTimeout(60000);
            connectionSocket.setKeepAlive(true);
            InputStream in = connectionSocket.getInputStream();

            //Build the response message
            StringBuilder sb = new StringBuilder();

            //Response headers
            sb.append("HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=utf-8\r\nConnection: keep-alive\r\n\r\n");

            //Data data data...
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
            int fromClientLength = in.read(fromClientBuffer, 0, 1024);

            if(fromClientLength >= 1024)
                while(fromClientLength > 4 && fromClientLength != -1)
                {
                    sb.append(new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8));
                    fromClientLength = in.read(fromClientBuffer, 0, 1024);
                    System.out.println("Length: " + fromClientLength);
                }
            else
                sb.append(new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8));

            byte[] encodedResponse = sb.toString().getBytes(StandardCharsets.UTF_8);

            connectionSocket.getOutputStream().write(encodedResponse);
            connectionSocket.close();
        }
    }
}
