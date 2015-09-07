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
	
	private static ArrayList<Peer> index;
	
	private static int id = 0;
	
	private static ArrayList<Peer> peerList;
	
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
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			
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
					
					
					dOut.writeInt(peerId);
					dOut.flush();
					break;
				case 1:
					String fileName = dIn.readUTF();
					
					if(search(fileName)){
						dOut.writeByte(1);
						dOut.writeInt(peerList.size());
						for(Peer p : peerList){
							dOut.writeUTF(p.getAddress() + " " + p.getPort());
							dOut.flush();
						}
						dOut.flush();
					}else {
						dOut.writeByte(0);
					}
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
		index.add(new Peer(peerId, numFiles, fileNames, directory, address, port));
	}
	
	public static Boolean search(String fileName){
		Boolean found = false;
		peerList = new ArrayList<Peer>();
		 for (Peer p : index){
			 if(p.searchFile(fileName)){
				 peerList.add(p);
				 found = true;
			 }
		 }
		 return found;
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
