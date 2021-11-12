package netJava2020MsC_fn26393_project_final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	private static final int SERVER_PORT = 4447;

	public static void main(String[] args) {

		try (Socket socket = new Socket("localhost", SERVER_PORT);
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedReader userReader = new BufferedReader(new InputStreamReader(System.in))) {

			System.out.println("Connected to the server.");

			Thread sendMessage = new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {

						String message = new String();
						try {
							message = userReader.readLine();

						} catch (IOException e) {
							System.out.println(e.getMessage());
						}

						writer.println(message);
					}
				}
			});
			sendMessage.start();

			while (!socket.isClosed()) {
				try {

					String message = reader.readLine();
					if (message != null) {
						System.out.println(message);
					}
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			
		} catch (UnknownHostException e1) {
			System.out.println(e1.getMessage());
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
	}
}
