package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CentralIndexingServer {
	
	private static HashMap<Integer,ArrayList<String>> index;
	
	private static int id = 0;
	
	private static ArrayList<Integer> peerList;
	
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
					//TODO: need to create an Object peer with more info, address and port
					int peerId = getUniqueId();
					
					Boolean end = false;
					ArrayList<String> fileNames = new ArrayList<String>();
					int numFiles = 0, port = 0;
					String directory = null, address = null;
					
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
						 	case 3:
						 		directory = dIn.readUTF();
						 		break;
						 	case 4:
						 		address = dIn.readUTF();
						 		break;
						 	case 5:
						 		port = dIn.readInt();
						 		break;
						 	default:
						 		end = true;
						 }
					}
					
					registry(peerId, numFiles, fileNames, directory, address, port);
					
					DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
					dOut.writeInt(peerId);
					dOut.flush();
					break;
				case 1:
					String fileName = dIn.readUTF();
					search(fileName);
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
	
	public static void registry(int peerId, int numFiles, ArrayList<String> fileNames, String directory, String address, int port){
		index.put(peerId, fileNames);
		//TODO: has to change how this will be register, but I need to think how the search will be done first
	}
	
	public static Boolean search(String fileName){
		Boolean found = false;
		peerList = new ArrayList<Integer>();
		 for (Map.Entry<Integer, ArrayList<String>> entry  : index.entrySet()){
			 for(String fn : entry.getValue()){
				 if(fn == fileName){
					 found = true;
					 peerList.add(entry.getKey());
					 //TODO: it has to be the address not the id, create a peer class
				 }
			 }
		 }
		 return found;
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
