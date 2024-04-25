package LibrarySystem;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Server {
	
private ServerSocket serverSocket;
public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

	public Server(ServerSocket serverSocket) {
		this.serverSocket=serverSocket;
	}
	
	public void startServer() {
		try {
			while(!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println("Connection established!");
				ClientHandler clientHandler = new ClientHandler(socket);	
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeServerSocket() {
		try {
			if(serverSocket !=null) {
				serverSocket.close();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		int port = 9806;
		System.out.println("Waiting for clients ...");
		ServerSocket serverSocket = new ServerSocket(port);
		Server server = new Server(serverSocket);
		server.startServer();
	}

	
}
