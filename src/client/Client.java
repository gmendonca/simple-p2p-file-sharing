package client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import util.Util;

public class Client {
	
	public static void main(String[] args) throws IOException {
    	
		if(args.length < 2){
    		System.out.println("It should be java client/Client folder port");
    		return;
    	}
		
    	//Server information
    	String serverAddress = "localhost";
    	int serverPort = 3434;
    	
    	if(args.length > 2){
    		try{
    			serverAddress = args[2];
        		serverPort = Integer.parseInt(args[3]);
    		} catch(Exception e){
    			System.out.println("It should be java client/Client folder port serverAddress serverPort");
    		}
    		
    	}
    	
    	String dir = args[0];
    	File folder = new File(dir);
    	int option;
    	String fileName = null;
    	
    	if(!folder.isDirectory()){
			System.out.println("Put a valid directory name");
			return;
    	}
    	
    	//Util.getExternalIP();
    	
    	String address = InetAddress.getLocalHost().getHostAddress();
    	int port = 3434;
    	try{
    		port = Integer.parseInt(args[1]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
    	final Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
    	Socket socket = null;
    	try {
    		socket = new Socket(serverAddress, serverPort);
    	}catch (IOException e){
    		System.out.println("There isn't any instance of server running. Start one first!");
    		return;
    	}
    	peer.register(socket);
    	
    	new Thread(){
    		public void run(){
    			try {
					peer.server();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}.start();
    	
    	new Thread(){
    		public void run(){
    			try {
					peer.income();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}.start();
    	
    	String [] peerAddress = new String[0];
    	
    	Scanner scanner = new Scanner(System.in);
    	while(true){
    		System.out.println("\n\nSelect the option:");
    		System.out.println("1 - Lookup for a file");
    		System.out.println("2 - Download file");
    		
    		option = scanner.nextInt();
    		int optpeer;
    		
    		if(option == 1){
    			System.out.println("Enter file name:");
    			fileName = scanner.next();
				peerAddress = peer.lookup(fileName, new Socket(serverAddress, serverPort), 1);
    		}
    		else if (option == 2){
    			if(peerAddress.length == 0){
    				System.out.println("Lookup for the peer first.");
    			}else if(peerAddress.length == 1 && Integer.parseInt(peerAddress[0].split(":")[2]) == peer.getPeerId()){
    				System.out.println("This peer has the file already, not downloading then.");
    			}else if(peerAddress.length == 1){
    				String[] addrport = peerAddress[0].split(":");
    				System.out.println("Downloading from peer " + addrport[2] + ": " + addrport[0] + ":" + addrport[1]);
    				peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
    			}else {
    				System.out.println("Select from which peer you want to Download the file:");
    				for(int i = 0; i < peerAddress.length; i++){
    					String[] addrport = peerAddress[i].split(":");
    					System.out.println((i+1) + " - " + addrport[0] + ":" + addrport[1]);
    				}
    				optpeer = scanner.nextInt();
    				while(optpeer > peerAddress.length || optpeer < 1){
    					System.out.println("Select a valid option:");
    					optpeer = scanner.nextInt();
    				}
    				String[] addrport = peerAddress[optpeer-1].split(":");
    				peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, -1);
    			}
    		}else{
    			scanner.close();
    			System.out.println("Peer desconnected!");
    			return;
    		}
    		
    	}
    }
}
