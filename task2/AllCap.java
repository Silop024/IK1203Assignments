import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class AllCap
{
    private static int BUFFERSIZE = 1024;

    public static void main(String[] args) throws Exception
    {
        int port = Integer.parseInt(args[0]);
        ServerSocket welcomeSocket = new ServerSocket(port);

        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();

            //byte[] buffer = new byte[BUFFERSIZE];

            //int length = connectionSocket.getInputStream().readAllBytes(buffer);

            //String sentence = new String(buffer, 0, length, StandardCharsets.UTF_8);
            System.out.println("hello");
            String sentence = new String(connectionSocket.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String cap = sentence.toUpperCase() + "\r\n";

            byte[] toClient = cap.getBytes(StandardCharsets.UTF_8);
            System.out.println("hello");
            connectionSocket.getOutputStream().write(toClient);

            connectionSocket.close();
        }
    }
}
