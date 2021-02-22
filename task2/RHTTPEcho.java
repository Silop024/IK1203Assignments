import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class RHTTPEcho
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
            OutputStream out = connectionSocket.getOutputStream();
            byte[] buffer = new byte[BUFFERSIZE];

            int requestLength = in.read(buffer);
            if(requestLength < 0)
            {
                break;
            }
            else
            {
                String response = new String(buffer, 0, requestLength) + "\r\n";
                byte[] toClient = response.getBytes();
                out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                out.write(toClient);
            }
            connectionSocket.close();


        }
    }
}
