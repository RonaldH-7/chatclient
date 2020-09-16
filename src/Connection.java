import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Connection {
	private boolean isServer;
	private ConnectionThread connectionThread = new ConnectionThread();
	private Consumer<String> incomingMessage;
	
	public Connection(boolean isServer, Consumer<String> incomingMessage) {
		this.incomingMessage = incomingMessage;
		this.isServer = isServer;
	}
	
	public void startConnection() {
		connectionThread.start();
	}
	
	public void sendMessage(String message) {
		try {
			connectionThread.output.writeUTF(message + "\n");
		} catch (Exception e) {
			System.err.println("Exception in sendMessage() of Connection class");
		}
	}
	
	public void endConnection() {
		try {
			connectionThread.socket.close();
		} catch (IOException e) {
			System.err.println("Exception in endConnection() of Connection class");
		}
	}
	
	protected boolean isServer() {
		return isServer;
	}
	
	private class ConnectionThread extends Thread {
		private Socket socket;
		private DataOutputStream output;
		private DataInputStream input;
		
		@Override
		public void run() {
			try {
				connect();
				
				while (true) {
					String message = input.readUTF();
					incomingMessage.accept(message);
				}
			} catch (Exception e) {
					incomingMessage.accept("Connection closed");
			}
		}
		
		private void connect() {
			try {
				if (isServer) {
					ServerSocket serverSocket = new ServerSocket(8888);
					socket = serverSocket.accept();
				} else {
					socket = new Socket("127.0.0.1", 8888);
				}
				
				output = new DataOutputStream(socket.getOutputStream());
				input = new DataInputStream(socket.getInputStream());
			} catch (Exception e) {
				System.err.println("Exception in connect() method of ConnectionThread class");
			}
		}
	}
}