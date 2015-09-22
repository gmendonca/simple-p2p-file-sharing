package bench;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import util.Util;
import client.Peer;

public class BenchSingleRegistry {
	
	private static String serverAddress = "localhost";
	private static int serverPort = 3434;
	
	
	public static void sendRequests(Peer peer, String fileName, int numRequests) throws IOException{
		long startTime = System.currentTimeMillis();
		
		@SuppressWarnings("unused")
		long start;
		for(int i = 0; i < numRequests; i++){
			start = System.currentTimeMillis();
			peer.lookup(fileName, new Socket(serverAddress, serverPort), i);
			//System.out.println("Took " + (System.currentTimeMillis() - start) + " ms.");
		}
		
		long stopTime = System.currentTimeMillis();
		
		//System.out.println("Overall -> Took " + Util.toSeconds(startTime, stopTime) + " s.");
		System.out.println("==============================================================================================");
		System.out.println("Overall - Peer " + peer.getPeerId() + " -> Took " + (stopTime-startTime) + " ms.");
		System.out.println("==============================================================================================");
		
		
	}

	public static void main(String[] args) throws IOException {
    	
    	if(args.length < 2){
    		System.out.println("It should be java bench/BenchSingleRegistry folder port");
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
    			System.out.println("It should be java bench/BenchSingleRegistry folder port serverAddress serverPort");
    		}
    		
    	}
    	
    	String dir = args[0];
    	File folder = new File(dir);
    	
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
    }
}
