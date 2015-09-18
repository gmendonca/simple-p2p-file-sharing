package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.PeerQueue;


public class CentralIndexingServer {
	
	private static Hashtable<String,ArrayList<Peer>> index;
	private static int port = 3434;
	private static int numThreads = 4;
	private static PeerQueue<Socket> peerQueue;
	
	public static Hashtable<String,ArrayList<Peer>> getIndex(){
		return index;
	}
	
	private static void server() throws IOException{
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		
		while(true){
			//System.out.println("Waiting for peer...");
			Socket socket = serverSocket.accept();
			synchronized(peerQueue){
				peerQueue.add(socket);
				//System.out.println("Added to queue.");
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	private static void income() throws IOException{
		
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		while(true){
			if(peerQueue.peek() == null){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			synchronized(peerQueue){
				//System.out.println("Added to executor");
				Server s = new Server(peerQueue.poll());
				executor.execute(s);
			}
		}
		
	}
	
	public static void registry(int peerId, int numFiles, ArrayList<String> fileNames, String directory, String address, int port){
		for(String fileName : fileNames){
			if(index.containsKey(fileName)){
				index.get(fileName).add(new Peer(peerId, numFiles, fileNames, directory, address, port));
			}else {
				index.put(fileName, new ArrayList<Peer>());
				index.get(fileName).add(new Peer(peerId, numFiles, fileNames, directory, address, port));
			}
			
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		index = new Hashtable<String, ArrayList<Peer>>();
		peerQueue = new PeerQueue<Socket>();
		if(args.length > 0){
			try{
	    		port = Integer.parseInt(args[1]);
	    	} catch (Exception e){
	    		System.out.println("Put a valid port number");
	    	}
		}
		
		new Thread() {
            public void run() {
                try {
                   server(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        new Thread() {
            public void run() {
                try {
                   income(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
	}
}
