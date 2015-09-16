package client;

import java.net.Socket;

public class Connection {
	
	private Socket socket;
	private String directory;
	
	public Connection(Socket socket, String directory){
		this.socket = socket;
		this.directory = directory;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public String getDirectory(){
		return directory;
	}

}
