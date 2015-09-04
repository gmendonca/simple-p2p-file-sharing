import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class CentralIndexingServer {
	
	private static HashMap<Integer,ArrayList<String>> index;
	
	private static int id = 0;
	
	private static int getUniqueId(){
		return ++id;
	}
	
	private static void server() throws IOException{
		
		ServerSocket serverSocket = new ServerSocket(3434);
		
		while(true){
			System.out.println("Waiting for peer...");
			Socket socket = serverSocket.accept();
			System.out.println("Peer connected...");
			
			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			
			byte option = dIn.readByte();
			
			switch(option){
				case 0:
					int peerId = getUniqueId();
					
					Boolean end = false;
					ArrayList<String> fileNames = new ArrayList<String>();
					int numFiles = 0;
					
					while(!end){
						byte messageType = dIn.readByte();
						
						 switch(messageType){
						 	case 1:
						 		numFiles = dIn.readInt();
						 		System.out.println(numFiles);
						 		break;
						 	case 2:
						 		for(int i = 0; i < numFiles; i++){
						 			fileNames.add(dIn.readUTF());
						 			System.out.println(fileNames.get(i));
						 		}
						 		break;
						 	default:
						 		end = true;
						 }
					}
					
					registry(peerId, fileNames);
					
					DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeInt(peerId);
					dOut.flush();
					break;
				case 1:
					break;
				default:					
				
			}
				
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void registry(int peerId, ArrayList<String> fileNames){
		index.put(peerId, fileNames);
	}
	
	public static void search(){
		
	}
	
	public static void main(String[] args) throws IOException {
		
		index = new HashMap<Integer,ArrayList<String>>();
		
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
