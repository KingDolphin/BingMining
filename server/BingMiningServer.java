package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class BingMiningServer extends Thread {

	public static final int DEFAULT_PORT = 9069;
	
	private List<ClientSocket> clients;
	private List<ConnectionCallback> connectionCallbacks;
	
	private ServerSocket listener;
	
	private boolean isRunning;
	
	public BingMiningServer(int port) {
		clients = new ArrayList<>();
		connectionCallbacks = new ArrayList<>();	
		isRunning = false;
		try {
			this.listener = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Server on port " + port + " already exists!");
			e.printStackTrace();
		}
	}
	
	public void log(Object msg) {
		System.out.println(msg);
	}
	
	public void addConnectionCallback(ConnectionCallback cc) {
		connectionCallbacks.add(cc);
	}
	
	public int getNumConnections() {
		return clients.size();
	}
	
	public void stopServer() {
		try {
			log("Stopping server...");
			for (ClientSocket cs : clients)
				cs.disconnect();
			isRunning = false;
			this.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void parseServerInput(String input) {
		switch (input) {
		case "quit":
			stopServer();
			break;
		default:
			log("Unknown command : " + input);
		}
	}
	
	private void parseInput(String input) {
		
	}
	
	@Override
	public void run() {
		log("Server started on port " + listener.getLocalPort() + "!");
		isRunning = true;
			
		// Poll for server input commands
		new Thread() {
			@Override
			public void run() {
				Scanner in = new Scanner(System.in);
				while (isRunning) {
					String input = in.nextLine();
					parseServerInput(input);
				}
				in.close();
			}
		}.start();
		
		// Poll for incoming connections
		try {
			while (isRunning) {
				Socket socket = listener.accept();
				ClientSocket cs = new ClientSocket(socket);
				cs.start();
			}
			listener.close();
		} catch (SocketException e) {
			isRunning = false;
		} catch (IOException e) {
			isRunning = false;
			e.printStackTrace();
		}
	}

	public class ClientSocket extends Thread {
	
		private Socket socket;
	
		private BufferedReader in;
		private PrintWriter out;
		
		private ClientSocket(Socket socket) {
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				System.err.println("Could not create input/output readers!");
				e.printStackTrace();
			}
		}
		
		// Poll for incoming messages
		@Override
		public void run() {
			boolean isRunning = true;
			try {
				clients.add(this);
				log(socket.getRemoteSocketAddress() + " has connected!");
				for (ConnectionCallback cc : connectionCallbacks)
					cc.onConnect(this);
				while (isRunning) {
					String input = in.readLine();
					if (input == null) // Client disconnected
						break;
			
					parseInput(input);
				}
			} catch (IOException e) {
				System.err.println("Cannot read line! For : " + socket.getRemoteSocketAddress());
			}
			this.disconnect();
		}
		
		
		public void disconnect() {
			log(socket.getRemoteSocketAddress() + " has disconnected!");
			try {
				clients.remove(this);
				socket.close(); // Disconnect socket
				for (ConnectionCallback cc : connectionCallbacks)
					cc.onDisconnect(this);
				this.join(); // Stop 
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}


}