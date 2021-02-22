import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class HastiTP {

	private static int BUFFERSIZE=1024;

	public static void main( String[] args)  throws IOException {

		int port = Integer.parseInt(args[0]);//
		ServerSocket welcomeSocket = new ServerSocket(port); //

		String status = "HTTP/1.1 200 OK\r\n\r\n";//


		while(true) {

			Socket connectionSocket = welcomeSocket.accept();//

			//StringBuilder sb = new StringBuilder();//

			byte[] fromClientBuffer = new byte[BUFFERSIZE];

			int fromClientLength = connectionSocket.getInputStream().read(fromClientBuffer,0, BUFFERSIZE);


			byte[] data;
			String combined;

			if (fromClientLength >= BUFFERSIZE)
			{

				data = connectionSocket.getInputStream().readAllBytes();
				String decodedStringfirst = new String(fromClientBuffer,0,fromClientLength, StandardCharsets.UTF_8);

				String decodedStringsecond = new String(data, StandardCharsets.UTF_8);

				combined= decodedStringfirst + decodedStringsecond;
			}

			else //when its less than buffer size
			{

				combined = new String(fromClientBuffer,0,fromClientLength, StandardCharsets.UTF_8);
			}
			// Compute response for client

			//String clientSentence = new String(fromClientBuffer, 0, fromClientLength, StandardCharsets.UTF_8);

			//String capitalizedSentence = clientSentence.toUpperCase()+'\n';

			byte[] statusByte = status.getBytes(StandardCharsets.UTF_8);
			connectionSocket.getOutputStream().write(statusByte);

			byte[] toClient = combined.getBytes(StandardCharsets.UTF_8);
			connectionSocket.getOutputStream().write(toClient);
			//sb.append(status+"\r\n");



			connectionSocket.close();
			//welcomeSocket.close();
		}

	}
}
