package bench;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import client.Peer;

public class RegistryThread extends Thread{
	private Peer peer;
	private String serverAddress;
	private int serverPort;
	
	public RegistryThread(Peer peer, String serverAddress, int serverPort){
		this.peer = peer;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}
	
	public void run(){
		try {
			peer.register(new Socket(serverAddress, serverPort));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
