package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import util.Util;


public class Peer {
	
	private int peerId;
	private int numFiles;
	private ArrayList<String> fileNames;
	private String directory;
	private String address;
	private int port;
	
	public Peer(String directory, ArrayList<String> fileNames, int numFiles, String address, int port){
		this.directory = directory;
		this.fileNames = fileNames;
		this.numFiles = numFiles;
		this.address = address;
		this.port = port;
		
	}
	
	//getters
		public int getPeerId(){
			return peerId;
		}
		
		public int getNumFiles(){
			return numFiles;
		}
		
		public ArrayList<String> getFileNames(){
			return fileNames;
		}
		
		public String getDirectory(){
			return directory;
		}
		
		public String getAddress(){
			return address;
		}
		
		public int getPort(){
			return port;
		}
		
		//setters
		public void setPeerId(int peerId){
			this.peerId = peerId;
		}
		
		public void setNumFiles(int numFiles){
			this.numFiles = numFiles;
		}
		
		public void setFileNames(ArrayList<String> fileNames){
			this.fileNames.addAll(fileNames);
		}
		
		public void addFileName(String fileName){
			this.fileNames.add(fileName);
		}
		
		public void setDirectory(String directory){
			this.directory = directory;
		}
		
		public void setAddress(String address){
			this.address = address;
		}
		
		public void setPort(int port){
			this.port = port;
		}
    
    public void register(String serverAddress, int serverPort) throws IOException {
    	System.out.println("Connecting to the server...");
    	Socket socket = new Socket(serverAddress, serverPort);
    	DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	
    	//Option to register in the server (new peer)
    	dOut.writeByte(0);
    
    	//Number of files
    	dOut.writeByte(1);
    	dOut.writeInt(numFiles);
    	dOut.flush(); 
    	//Files names
    	dOut.writeByte(2);
    	for(String str : fileNames)
    		dOut.writeUTF(str);
    	dOut.flush();
    	dOut.writeByte(3);
    	dOut.writeUTF(directory);
    	dOut.flush();
    	dOut.writeByte(4);
    	dOut.writeUTF(address);
    	dOut.flush();
    	dOut.writeByte(5);
    	dOut.writeInt(port);
    	dOut.flush();
    	dOut.writeByte(-1);
    	dOut.flush();
    	
    	
    	//Reading the Unique Id from the Server
    	DataInputStream dIn = new DataInputStream(socket.getInputStream());
    	this.peerId = dIn.readInt();
    	
    	dOut.close();
    	dIn.close();
    	
    	System.out.println("Running as Peer " + peerId + "!");
    	
    	socket.close();
	}

    public String[] lookup(String fileName) throws IOException{
    	//System.out.println("Connecting to the server...");
    	Socket socket = new Socket("localhost", 3434);
    	DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	
    	//Option to look for a file
    	dOut.writeByte(1);
    	
    	String [] peerAddress = new String[0];
    	
    	//File name
    	dOut.writeUTF(fileName);
    	dOut.flush();
    	//System.out.println("Reading from the server...");
    	
    	//Reading the peer Address that has the file
    	DataInputStream dIn = new DataInputStream(socket.getInputStream());
    	byte found = dIn.readByte();
    	
    	if(found == 1){
    		int qt = dIn.readInt();
    		peerAddress = new String[qt];
    		for(int i = 0; i < qt; i++){
    			peerAddress[i] = dIn.readUTF();
    			//System.out.println("Peer " + peerAddress[i] + " has the file " + fileName + "!");
    		}
    	} else if(found == 0){
    		//System.out.println("File not found in the system");
    		peerAddress = new String[0];
    	}
    	
    	dOut.close();
    	dIn.close();

    	socket.close();
    	return peerAddress;
    }
    
    public void server() throws IOException{
		
		@SuppressWarnings("resource")
		ServerSocket serverSocket = new ServerSocket(port);
		
		while(true){
			System.out.println("Waiting for peer...");
			Socket socket = serverSocket.accept();
			new Server(socket, directory).start();
		}
		
	}
    
    public void download(String peerAddress, int port, String fileName)  throws IOException {
    	Socket socket = new Socket("localhost", port);
    	DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	dOut.writeUTF(fileName);
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream(fileName);
        Util.copy(in, out);
        System.out.println("File " + fileName + " recieved from peer " + peerAddress + ":" + port);
        dOut.close();
        out.close();
        in.close();
        socket.close();
    }
    
    
}

	
