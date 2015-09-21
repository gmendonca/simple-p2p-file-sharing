package bench;

import java.net.Socket;

import client.Peer;

public class RegistryThread extends Thread{
	private Peer peer;
	private Socket socket;
	
	public RegistryThread(Peer peer, Socket socket){
		this.peer = peer;
		this.socket = socket;
	}
	
	public void run(){
		try {
			peer.register(socket);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
