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
import java.net.ServerSocket;
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
    
    private void register(String serverAddress, int serverPort) throws IOException {
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
    	System.out.println("Connecting to the server...");
    	Socket socket = new Socket("localhost", 3434);
    	DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	
    	//Option to look for a file
    	dOut.writeByte(1);
    	
    	String [] peerAddress = new String[0];
    	
    	//File name
    	dOut.writeUTF(fileName);
    	dOut.flush();
    	System.out.println("Reading from the server...");
    	//Reading the peer Address that has the file
    	DataInputStream dIn = new DataInputStream(socket.getInputStream());
    	byte found = dIn.readByte();
    	
    	if(found == 1){
    		int qt = dIn.readInt();
    		peerAddress = new String[qt];
    		for(int i = 0; i < qt; i++){
    			peerAddress[i] = dIn.readUTF();
    			System.out.println("Peer " + peerAddress + " has the file " + fileName + "!");
    		}
    	} else if(found == 0){
    		System.out.println("File not found in the system");
    		peerAddress = new String[0];
    	}
    	
    	dOut.close();
    	dIn.close();

    	socket.close();
    	return peerAddress;
    }
    
    private void server() throws IOException{
		
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
        dOut.close();
        out.close();
        in.close();
        socket.close();
    }
    
    public static void main(String[] args) throws IOException {
    	
    	
    	if(args.length < 2){
    		System.out.println("It should be java/client folder port");
    		return;
    	}
    	//Server information
    	String serverAddress = "localhost";
    	int serverPort = 3434;
    	
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
    	int port = 3434;
    	try{
    		port = Integer.parseInt(args[1]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
    	final Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
    	peer.register(serverAddress, serverPort);
    	
    	new Thread(){
    		public void run(){
    			try {
					peer.server();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}.start();
    	
    	String [] peerAddress = new String[0];
    	
    	Scanner scanner = new Scanner(System.in);
    	while(true){
    		System.out.println("Select the option:");
    		System.out.println("1 - Lookup for a file");
    		System.out.println("2 - Download file");
    		
    		option = scanner.nextInt();
    		int optpeer;
    		
    		if(option == 1){
    			System.out.println("Enter file name:");
    			fileName = scanner.next();
    			peerAddress = peer.lookup(fileName);
    		}
    		else if (option == 2){
    			if(peerAddress.length == 0){
    				System.out.println("Lookup for the peer first.");
    			}else if(peerAddress.length == 1){
    				String[] addrport = peerAddress[0].split(":");
    				peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName);
    			}else {
    				System.out.println("Select from which peer you want to Download the file:");
    				for(int i = 0; i < peerAddress.length; i++){
    					System.out.println((i+1) + " - " + peerAddress[i]);
    				}
    				optpeer = scanner.nextInt();
    				while(optpeer > peerAddress.length || optpeer < 1){
    					System.out.println("Select a valid option:");
    					optpeer = scanner.nextInt();
    				}
    				String[] addrport = peerAddress[optpeer-1].split(":");
    				peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName);
    			}
    		}else{
    			scanner.close();
    			System.out.println("Peer desconnected!");
    			return;
    		}
    		
    	}
    }
}

	
