package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ArrayBlockingQueue;


public class CentralIndexingServer {
	
	private static Hashtable<String,ArrayList<Peer>> index;
	private static int port = 3434;
	private static ArrayBlockingQueue<Socket> peerQueue;
	
	public static Hashtable<String,ArrayList<Peer>> getIndex(){
		return index;
	}
	
	private static int id = 0;
	
	@SuppressWarnings("unused")
	private static int getUniqueId(){
		return ++id;
	}
	
	private static void server() throws IOException{
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		
		while(true){
			//System.out.println("Waiting for peer...");
			Socket socket = serverSocket.accept();
			peerQueue.add(socket);
			//new Server(socket).start();
			
		}
		
	}

	private static void income() throws IOException{
		
		while(true){
			if(peerQueue.peek() == null)
				continue;
			
			new Server(peerQueue.poll()).start();
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
