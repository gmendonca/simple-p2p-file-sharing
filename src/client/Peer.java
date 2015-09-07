package client;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

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
    
    private void register() throws IOException {
    	System.out.println("Connecting to the server...");
    	Socket socket = new Socket("localhost", 3434);
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

    public String lookup(String fileName) throws IOException{
    	System.out.println("Connecting to the server...");
    	Socket socket = new Socket("localhost", 3434);
    	DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	
    	//Option to look for a file
    	dOut.writeByte(1);
    	
    	String peerAddress = "localhost";
    	
    	//File name
    	dOut.writeUTF(fileName);
    	dOut.flush();
    	System.out.println("Reading from the server...");
    	//Reading the peer Address that has the file
    	DataInputStream dIn = new DataInputStream(socket.getInputStream());
    	byte found = dIn.readByte();
    	
    	if(found == 1){
    		int qt = dIn.readInt();
    		for(int i = 0; i < qt; i++){
    			peerAddress = dIn.readUTF();
    			System.out.println("Peer " + peerAddress + " has the file " + fileName + "!");
    		}
    	} else if(found == 0){
    		System.out.println("File not found in the system");
    	}
    	
    	dOut.close();
    	dIn.close();

    	socket.close();
    	return peerAddress;
    }
    
    public void download(String fileName)  throws IOException {
    	Socket socket = new Socket("localhost", 3434);
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream(fileName);
        Util.copy(in, out);
        out.close();
        in.close();
    }
    
    public static void main(String[] args) throws IOException {
    	
    	String dir = args[0];
    	File folder = new File(dir);
    	int option;
    	String fileName = null;
    	
    	if(!folder.isDirectory()){
			System.out.println("Put a valid directory name");
			return;
    	}
    	
    	URL url = new URL("http://checkip.amazonaws.com/");
    	BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
    	System.out.println(br.readLine());
    	
    	String address = InetAddress.getLocalHost().getHostAddress();
    	//TODO: ask the user for the port
    	int port = 3434;
    	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
    	Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
    	peer.register();
    	
    	//TODO: create a thread for incoming requests (server side)
    	new Server(port, dir).start();
    	
    	String peerAddress = "localhost";
    	
    	Scanner scanner = new Scanner(System.in);
    	while(true){
    		System.out.println("Select the option:");
    		System.out.println("1 - Lookup for a file");
    		System.out.println("2 - Download file");
    		
    		option = scanner.nextInt();
    		
    		if(option == 1){
    			System.out.println("Enter file name:");
    			fileName = scanner.next();
    			peerAddress = peer.lookup(fileName);
    		}
    		else if (option == 2){
    			if(fileName != null){
    				System.out.println("Enter file name:");
        			fileName = scanner.next();
    			}
    			peer.download(peerAddress, fileName);
    		}else{
    			scanner.close();
    			System.out.println("Peer desconnected!");
    			return;
    		}
    		
    	}
    }
}

	
