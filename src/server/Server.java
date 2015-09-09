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
	
	public void newPeerList(){
		peerList = new ArrayList<Peer>();
	}
	
	public void addPeer(Peer peer){
		peerList.add(peer);
	}
	
	public Server(Socket socket){
		this.socket = socket;
	}
	
	public Boolean search(String fileName){
		Boolean found = false;
		newPeerList();
		 for (Peer p : CentralIndexingServer.getIndex()){
			 if(p.searchFile(fileName)){
				 addPeer(p);
				 found = true;
			 }
		 }
		 return found;
	}
	
	public void run(){
		
		try{
			//System.out.println("Peer connected...");
			
			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
			
			byte option = dIn.readByte();
			
			switch(option){
				case 0:
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
					System.out.println(dIn.readUTF());
					Boolean b = search(fileName);
					//TODO: see if I can do this with wait and notify, or find a better time and took it out from the overall time
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(b){
						dOut.writeByte(5);
						dOut.writeInt(peerList.size());
						dOut.flush();
						for(Peer p : peerList){
							dOut.writeUTF(p.getAddress() + ":" + p.getPort());
							dOut.flush();
						}
					}else {
						dOut.writeByte(0);
						dOut.flush();
					}
					break;
				default:					
				
			}
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
		
	}
}
