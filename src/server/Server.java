package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
	
	private static int id = 0;
	
	private static ArrayList<Peer> peerList;
	private Socket socket;
	
	private int getUniqueId(){
		synchronized(this){
			return ++id;
		}
	}
	
	public static void newPeerList(){
		peerList = new ArrayList<Peer>();
	}
	
	public static void addPeer(Peer peer){
		peerList.add(peer);
	}
	
	public Server(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		
		try{
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
					
					synchronized(this){
						CentralIndexingServer.registry(peerId, numFiles, fileNames, directory, address, port);
					}
					
					
					dOut.writeInt(peerId);
					dOut.flush();
					break;
				case 1:
					String fileName = dIn.readUTF();
					
					if(CentralIndexingServer.search(fileName)){
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
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
		
	}
}
