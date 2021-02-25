import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class ConcHTTPAsk implements Runnable
{
    private Socket connectionSocket;

    public ConcHTTPAsk(Socket s)
    {
        this.connectionSocket = s;
    }

    public void run()
    {
        try
        {
            InputStream fromClient = connectionSocket.getInputStream();
            OutputStream toClient = connectionSocket.getOutputStream();

            //Get array containing request type, address, http version
            String[] requestLine;
            String[] parameters = new String[3];
            String response = "";
            try
            {
                requestLine = getRequest(fromClient);

                if(!requestLine[1].startsWith("/ask"))
                {
                    toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                    connectionSocket.close();
                }
                else
                {
                    parameters = getParameters(requestLine[1]);

                    if(parameters[2] != null)
                        response = TCPClient.askServer(parameters[0], Integer.parseInt(parameters[1]), parameters[2]);
                    else
                        response = TCPClient.askServer(parameters[0], Integer.parseInt(parameters[1]));
                }
            }
            catch(Exception e)
            {
                toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                connectionSocket.close();
            }

            if(!connectionSocket.isClosed())
            {
                toClient.write("HTTP/1.1 200 OK\r\n".getBytes());
                toClient.write(response.getBytes());
                connectionSocket.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("Thread couldn't execute properly");
        }
    }


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
            Socket s = welcomeSocket.accept();
            System.out.println("Accepted");
            s.setSoTimeout(60000);
            Runnable r = new ConcHTTPAsk(s);
            new Thread(r).start();
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


}
