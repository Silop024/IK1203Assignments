import tcpclient.TCPClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;


public class HTTPAsk
{
    public static void main(String[] args) throws Exception
    {
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            System.err.println("Usage: TCPEcho port");
            System.exit(1);
        }
        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true) {
            //Establish the TCP connection with the client
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("Accepted");
            connectionSocket.setSoTimeout(60000);

            InputStream fromClient = connectionSocket.getInputStream();
            OutputStream toClient = connectionSocket.getOutputStream();

            //Get array containing request type, address, http version
            String[] requestLine;
            String[] parameters;
            String response = "";
            try {
                requestLine = getRequest(fromClient);

                if (!(requestLine[1].startsWith("/ask") && requestLine[0].equals("GET") && requestLine[2].equals("HTTP/1.1"))) {
                    toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                    connectionSocket.close();
                } else {
                    parameters = getParameters(requestLine[1]);

                    response = parameters[2] != null ?
                            TCPClient.askServer(parameters[0], Integer.parseInt(parameters[1]), parameters[2] + "\r\n") :
                            TCPClient.askServer(parameters[0], Integer.parseInt(parameters[1]));
                }
            } catch (Exception e) {
                toClient.write("HTTP/1.1 400 Bad Request\r\n\r\n".getBytes());
                connectionSocket.close();
            }

            if (!connectionSocket.isClosed()) {
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
        try {
            //Get request line
            String requestMessage = new String(byteMessage, StandardCharsets.UTF_8);
            String[] splitRequestMessage = requestMessage.split("\r\n");
            String requestLine = splitRequestMessage[0];

            return requestLine.split(" ");
        } catch (Exception ex) {
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
        for (int i = 1; i < p.length; i += 2) {
            parameters[j++] = p[i];
        }

        return parameters;
    }
}
