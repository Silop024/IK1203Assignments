import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class HTTPAsk
{
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
        ServerSocket welcomeSocket = new ServerSocket(port);

        while(true)
        {
            //Establish the TCP connection with the client
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Accepted");
            connectionSocket.setSoTimeout(60000);

            InputStream fromClient = connectionSocket.getInputStream();

            //Get array containing request type, address, http version
            String[] requestLine = getRequest(fromClient);

            System.out.println("Request type: " + requestLine[0]);
            System.out.println("URL: " + requestLine[1]);
            System.out.println("HTTP version: " + requestLine[2]);

            String[] urlComponents = getParameters(requestLine[1], requestLine[3]);

            System.out.println("\nHostname: " + urlComponents[0]);
            System.out.println("Port: " + urlComponents[1]);
            System.out.println("String (if any): " + urlComponents[2]);

            if(urlComponents[2] != null)
                TCPClient.askServer(urlComponents[0], Integer.parseInt(urlComponents[1]), urlComponents[2]);
            else
                TCPClient.askServer(urlComponents[0], Integer.parseInt(urlComponents[1]));
        }
    }

    private static String[] getRequest(InputStream reqMsg) throws Exception
    {
        byte[] byteMessage = new byte[2048];
        reqMsg.read(byteMessage);

        String[] request = new String[0];
        try
        {
            //Get request line
            String requestMessage = new String(byteMessage, StandardCharsets.UTF_8);
            System.out.println(requestMessage);
            String[] splitRequestMessage = requestMessage.split("\r\n");
            String requestLine = splitRequestMessage[0];

            String hostLine = splitRequestMessage[1];
            String hostname = getHostname(hostLine);

            requestLine += " " + hostname;

            System.out.println("Request line: " + requestLine);

            return requestLine.split(" ");
        }
        catch(Exception ex)
        {
            System.err.println("Request in wrong format");
            System.exit(1);
        }
        return request;
    }

    private static String[] getParameters(String url, String host) throws MalformedURLException
    {
        String[] components = new String[3];
        try
        {
            URI u = new URI(url);

            //Get hostname and port
            String[] hostParts = host.split(":");
            components[0] = hostParts[0];
            System.out.println("Hostname: " + components[0]);
            components[1] = hostParts[1];
            System.out.println("Port: " + components[1]);

            //Get string parameter
            components[2] = u.getFragment();
            System.out.println("Fragment: " + components[2]);

        }
        catch(Exception ex)
        {
            System.err.println("URL in wrong format");
            System.exit(1);
        }
        return components;
    }

    private static String getHostname(String h)
    {
        String[] hostLine = h.split(" ");
        String hostname = hostLine[1];

        return hostname;
    }
}
