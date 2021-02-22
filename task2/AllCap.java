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

            byte[] buffer = new byte[BUFFERSIZE];

            int length = connectionSocket.getInputStream().read(buffer);

            String sentence = new String(buffer, 0, length, StandardCharsets.UTF_8);
            String cap = '\n' + sentence.toUpperCase() + '\n';

            byte[] toClient = cap.getBytes(StandardCharsets.UTF_8);

            connectionSocket.getOutputStream().write(toClient);

            connectionSocket.close();
        }
    }
}
