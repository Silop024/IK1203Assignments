import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

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


            String statusCode = "";

            //Buffer for client requests
            byte[] fromClientBuffer = new byte[BUFFERSIZE];
            //Read data from input to the buffer, also saves length of input
            int fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer);
            connectionSocket.setKeepAlive(true);

            //Get rid of unused buffer space
            byte[] byteMessage = new byte[fromClientLength];
            for(int i = 0; i < fromClientLength; i++)
                byteMessage[i] = fromClientBuffer[i];

            //Make status line by first taking the request line of the request
            //message.
            //It then splits the request line into its three components, i
            //only use the method field and HTTP version field
            String requestMessage = "";
            String requestLine = "";
            String method = "";
            String httpVersion = "HTTP/1.1";
            try
            {
                requestMessage = new String(byteMessage, StandardCharsets.UTF_8);
                String[] splitRequestMessage = requestMessage.split("\r\n");
                requestLine = splitRequestMessage[0];
            }
            catch(Exception e)
            {
                statusCode = "400 Bad Request\r\n";
            }
            try
            {
                String[] splitRequestLine = requestLine.split(" ");
                method = splitRequestLine[0];
                httpVersion = splitRequestLine[2];
            }
            catch(Exception ex)
            {
                statusCode = "400 Bad Request\r\n";
            }
            switch(method)
            {
                case "GET":
                    statusCode = "200 OK\r\n";
                    break;
                case "PUT":
                    statusCode = "501 Not Implemented\r\n";
                    break;
                case "POST":
                    statusCode = "501 Not Implemented\r\n";
                    break;
            }
            statusCode = httpVersion + " " + statusCode;

            //Get response header
            String responseMessage =
            "Content-Type: text/plain; charset=utf-8\r\nConnection: keep-alive\r\n\r\n" + requestMessage;

            responseMessage = statusCode + responseMessage + "\r\n";


            byte[] encodedResponse = responseMessage.getBytes(StandardCharsets.UTF_8);


            connectionSocket.getOutputStream().write(encodedResponse);
            connectionSocket.close();
        }
    }
}
