package server;

import java.net.Socket;
import java.util.LinkedList;

public class PeerQueue {
	
	private LinkedList<Socket> queue;
	
	public Socket peek(){
		return queue.peek();
	}
	
	public Socket poll(){
		return queue.poll();
	}
	
	public void add(Socket socket){
		queue.add(socket);
		
	}
	
	public PeerQueue(){
		queue = new LinkedList<Socket>();
	}

}
