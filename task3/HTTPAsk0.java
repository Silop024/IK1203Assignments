import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class HTTPAsk0
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
            OutputStream toClient = connectionSocket.getOutputStream();

            //Get array containing request type, address, http version
            String[] requestLine;
            String[] parameters = new String[3];
            String response = "";
            try
            {
                requestLine = getRequest(fromClient);

                if(!(requestLine[1].startsWith("/ask") || requestLine[0].equals("GET") || requestLine[2].equals("HTTP/1.1")))
                {
                    toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                    connectionSocket.close();
                }
                else
                {
                    parameters = getParameters(requestLine[1]);

                    if(parameters[2] != null)
                        response = askServer(parameters[0], Integer.parseInt(parameters[1]), parameters[2] + "\r\n\r\n");
                    else
                        response = askServer(parameters[0], Integer.parseInt(parameters[1]));
                }
            }
            catch(Exception e)
            {
                toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                connectionSocket.close();
            }

            if(!connectionSocket.isClosed())
            {
                toClient.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                toClient.write(response.getBytes());
                connectionSocket.close();
            }
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
            String[] splitRequestMessage = requestMessage.split("\r\n");
            String requestLine = splitRequestMessage[0];

            return requestLine.split(" ");
        }
        catch(Exception ex)
        {
            System.err.println("Request in wrong format");
            System.exit(1);
        }
        return request;
    }

    private static String[] getParameters(String url) throws Exception
    {
        String[] parameters = new String[3];
        URI uri = new URI(url);

        String paramString = uri.getQuery();
        String[] p = paramString.split("&|=");

        int j = 0;
        for(int i = 1; i < p.length; i += 2)
            parameters[j++] = p[i];

        return parameters;
    }

    private static String askServer(String name, int port, String toServer) throws Exception
    {
        int buffersize = 1024;

        byte[] buffer = new byte[buffersize];
        byte[] toServerByte = toServer.getBytes(StandardCharsets.UTF_8);

        Socket cs = new Socket(name, port);

        int toServerLen = toServerByte.length;
        cs.getOutputStream().write(toServerByte, 0, toServerLen);

        int fromServerLen = 0;
        StringBuilder sb = new StringBuilder();
        long lap = 0;
        long start = System.currentTimeMillis();

        while(true)
        {
            try
            {
                fromServerLen = cs.getInputStream().read(buffer);
            }
            catch(Exception e)
            {
                break;
            }
            if(fromServerLen == -1)
                break;
            if(lap - start > 10000)
                break;
            String decodedString = new String(buffer, 0, fromServerLen, StandardCharsets.UTF_8);
            sb.append(decodedString);
        }
        cs.close();
        return sb.toString();
    }
    private static String askServer(String name, int port) throws Exception
    {
        int buffersize = 1024;

        byte[] buffer = new byte[buffersize];

        Socket cs = new Socket(name, port);

        int fromServerLen = 0;
        StringBuilder sb = new StringBuilder();

        while(true)
        {
            try
            {
                fromServerLen = cs.getInputStream().read(buffer);
            }
            catch(Exception e)
            {
                break;
            }
            if(fromServerLen == -1)
                break;
            String decodedString = new String(buffer, 0, fromServerLen, StandardCharsets.UTF_8);
            sb.append(decodedString);
        }
        cs.close();
        return sb.toString();
    }
}
