package LibrarySystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
	
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			if (this.socket == null) {
	            throw new IllegalArgumentException("Socket must not be null");
	        }
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientHandlers.add(this);
		} catch(IOException e) {
			closeEverytthing(socket, bufferedReader, bufferedWriter);
		}
	}
	
	@Override
	public void run() {
		while(socket.isConnected()) {
			try {
				String messageFromClient = bufferedReader.readLine();
				broadcastMessage(messageFromClient);
			} catch(IOException e) {
				closeEverytthing(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}
	
	public synchronized static void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
        	try {
        		clientHandler.bufferedWriter.write(messageToSend);
        		clientHandler.bufferedWriter.newLine();
        		clientHandler.bufferedWriter.flush();
        	} catch(IOException e) {
        		e.printStackTrace();
        	}
        }
    }
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
	}
	
	public void closeEverytthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		removeClientHandler();
		try {
			if (bufferedReader!=null) {
				bufferedReader.close();
			}
			if (bufferedWriter!=null) {
				bufferedWriter.close();
			}
			if (socket!=null) {
				socket.close();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
