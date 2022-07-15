import tcpclient.TCPClient;

import java.io.IOException;


public class TCPAsk
{
    public static void main(String[] args)
    {
        String hostname = null;
        int port = 0;
        String userInput = null;

        try {
            hostname = args[0];
            port = Integer.parseInt(args[1]);
            if (args.length >= 3) {
                StringBuilder builder = new StringBuilder();
                boolean first = true;

                for (int i = 2; i < args.length; i++) {
                    if (first) {
                        first = false;
                    } else {
                        builder.append(" ");
                    }
                    builder.append(args[i]);
                }
                builder.append("\n");
                userInput = builder.toString();
            }
        } catch (Exception ex) {
            System.err.println("Usage: TCPAsk host port <data to server>");
            System.exit(1);
        }

        try {
            String serverOutput = userInput != null ? TCPClient.askServer(hostname, port, userInput) : TCPClient.askServer(hostname, port);
            System.out.printf("%s:%d says:\n%s", hostname, port, serverOutput);
        } catch (IOException ex) {
            System.err.println(ex.getCause().toString());
            System.exit(1);
        }
    }
}
