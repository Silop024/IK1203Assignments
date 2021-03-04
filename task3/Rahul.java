import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class Rahul
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
            Socket cs = welcomeSocket.accept();
            InputStream in = cs.getInputStream();
            OutputStream out = cs.getOutputStream();

            String hostname = null;
            String ToServer = null;
            String cport = null;

            byte[] fromClient = new byte[1024];
            int fromClientLen = in.read(fromClient);
            String response = new String(fromClient, 0, fromClientLen);
            String[] request = response.split("[ =&?/]");
            for(int i = 0; i < request.length; i++)
            {
                if(request[i].equals("hostname")){hostname = request[i+1];}
                else if(request[i].equals("port")){cport = request[i+1];}
                else if(request[i].equals("string")){ToServer = request[i+1] + "\r\n";}
            }
            try
            {
                out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                out.write((TCPClient.askServer(hostname, Integer.parseInt(cport), ToServer) + "\r\n\r\n").getBytes());
            }
            catch(Exception e)
            {
                out.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                e.printStackTrace();
            }
            cs.close();
        }
    }

    public static String askRahul(String hostname, int port, String toServer) throws Exception
    {
        byte[] from = new byte[1024];
        Socket rahulSocket = new Socket(hostname, port);
        rahulSocket.getOutputStream().write(toServer.getBytes());
        int fromServerLength = rahulSocket.getInputStream().read(from);
        String a = new String(from, 0, fromServerLength);
        rahulSocket.close();
        return a;
    }
}
