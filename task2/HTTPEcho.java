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
            //Read data from input to the buffer, also saves length in nr of bytes?
            int fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer);


            //Make status line by first taking the request line of the request
            //message.
            //It then splits the request line into its three components, i
            //only use the method field and HTTP version field
            String requestMessage = new String(fromClientBuffer, StandardCharsets.UTF_8);
            String[] splitRequestMessage = requestMessage.split("\r\n");
            String requestLine = splitRequestMessage[0];

            String[] splitRequestLine = requestLine.split(" ");
            String method = splitRequestLine[0];
            String httpVersion = splitRequestLine[2];

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
            }
            statusCode = httpVersion + " " + statusCode;

            //Get response header
            String responseMessage =
            "Content-Type: text/plain; charset=utf-8\r\nConnection: close\r\n\r\n" + requestMessage;

            responseMessage = statusCode + responseMessage + "\r\n";


            byte[] encodedResponse = responseMessage.getBytes(StandardCharsets.UTF_8);


            //Respond with the data that was given and status code
            connectionSocket.getOutputStream().write(encodedResponse, 0, encodedResponse.length);
            connectionSocket.close();
        }
    }
}
