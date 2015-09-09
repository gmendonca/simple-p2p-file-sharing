package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class CentralIndexingServer {
	
	private static ArrayList<Peer> index;
	
	public static ArrayList<Peer> getIndex(){
		return index;
	}
	
	private static int id = 0;
	
	@SuppressWarnings("unused")
	private static int getUniqueId(){
		return ++id;
	}
	
	private static void server() throws IOException{
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(3434);
		
		while(true){
			//System.out.println("Waiting for peer...");
			Socket socket = serverSocket.accept();
			new Server(socket).start();
		}
		
	}
	
	public static void registry(int peerId, int numFiles, ArrayList<String> fileNames, String directory, String address, int port){
		index.add(new Peer(peerId, numFiles, fileNames, directory, address, port));
	}
	
	public static void main(String[] args) throws IOException {
		
		index = new ArrayList<Peer>();
		
		new Thread() {
            public void run() {
                try {
                   server(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
	}
}
