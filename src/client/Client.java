package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
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
    	
    	//URL url = new URL("http://checkip.amazonaws.com/");
    	//BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
    	//System.out.println(br.readLine());
    	
    	String address = InetAddress.getLocalHost().getHostAddress();
    	int port = 3434;
    	try{
    		port = Integer.parseInt(args[1]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
    	final Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
    	peer.register(new Socket(serverAddress, serverPort));
    	
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
				peerAddress = peer.lookup(fileName, new Socket(serverAddress, serverPort), 1);
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
