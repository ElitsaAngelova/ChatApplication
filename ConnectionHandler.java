package netJava2020MsC_fn26393_project_final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ConnectionHandler implements Runnable{
	
	private static final int LAST_INDEX_OF_LINE = 3;
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	Scanner scan = new Scanner(System.in);
	private Socket socket;
	private String nickname;
	private String email;
	private BufferedReader in;
	private PrintWriter out;
	private int id;
	
	private void getClientData() {
		
		String inputLine;
		try {
			do {
				out.println("Enter nickname:");
				inputLine = in.readLine();
				if (ChatServer.activeClientsNames.contains(inputLine)) {
					this.getOut().println(("This nickname is taken!"));
				}
			} while(ChatServer.activeClientsNames.contains(inputLine));
			
			setNickname(inputLine);
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		out.println("Enter email:");
		try {
			inputLine = in.readLine();
			setEmail(inputLine);
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		DBManager.addUser(getId(), nickname, email);
	}

	public ConnectionHandler(Socket socket, String nickname, BufferedReader in, PrintWriter out, int id) {
		this.socket = socket;
		this.nickname = nickname;
		this.in = in;
		this.out = out;
		this.id = id;
	}

	@Override
	public void run() {
		getClientData();
		ChatServer.activeClients.add(this);
		ChatServer.getActiveClientsNames().add(this.getNickname());
		out.println(this.getNickname() + " logged.");
		
		while (!socket.isClosed()) {
			String inputLine;
			try {
				inputLine = in.readLine();
				if (inputLine != null) {
					if (inputLine.equals("disconnect")) {
						try {
							this.socket.close();
						} catch (IOException e) {
							System.out.println(e.getMessage());
						}
						ChatServer.getActiveClients().remove(this);
						ChatServer.getActiveClientsNames().remove(this.getNickname());
						DBManager.removeUser(this.getId());
						break;
					} else if (inputLine.equals("list-users")) {
						for (String name : ChatServer.getActiveClientsNames()) {
							getOut().println(name);
							DBManager.showUsers();
						}
					} else if (inputLine.equals("download-image")) {
						DownloadFile.download(this.getNickname());
						getOut().println("File downloaded!");
					} else if (inputLine.split(" ", 2)[0].equals("send-all")) {
						String message = inputLine.split(" ", 2)[1];
						for (ConnectionHandler clientHandler : ChatServer.getActiveClients()) {
							clientHandler.getOut().println("[" + LocalDateTime.now().format(formatter) + "] " + this.getNickname() + ": " + message);
						}
					} else if (inputLine.split(" ", 2)[0].equals("send-email")) {
						String recipient = inputLine.split(" ", LAST_INDEX_OF_LINE)[1];
						String message = inputLine.split(" ", LAST_INDEX_OF_LINE)[2];
						for (ConnectionHandler clientHandler : ChatServer.getActiveClients()) {
							if (clientHandler.getNickname().equals(recipient)) {
								Mail.sendEmail(DBManager.getEmail(clientHandler.getId()), DBManager.getEmail(this.getId()), message);
								this.getOut().println(("Email sent!"));
							}
						}
						if(!ChatServer.activeClientsNames.contains(recipient)) {
							this.getOut().println(("User [" + recipient + "] seems to be offline"));
						}
					} else if (inputLine.split(" ", LAST_INDEX_OF_LINE)[0].equals("send")) {
						String recipient = inputLine.split(" ", LAST_INDEX_OF_LINE)[1];
						String message = inputLine.split(" ", LAST_INDEX_OF_LINE)[2];

						for (ConnectionHandler clientHandler : ChatServer.getActiveClients()) {
							if (clientHandler.getNickname().equals(recipient)) {
								clientHandler.getOut().println(
										("[" + LocalDateTime.now().format(formatter) + "] " + this.getNickname() + ": " + message));
							}
						}
						if(!ChatServer.activeClientsNames.contains(recipient)) {
							this.getOut().println(("User [" + recipient + "] seems to be offline"));
						}
					}
				}
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		}

	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BufferedReader getIn() {
		return in;
	}

	public void setIn(BufferedReader in) {
		this.in = in;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
