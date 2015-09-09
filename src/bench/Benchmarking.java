package bench;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

import util.Util;
import client.Peer;

public class Benchmarking {
	
	
	public static void sendRequests(Peer peer, String fileName, int numRequests) throws IOException{
		long startTime = System.currentTimeMillis();
		
		@SuppressWarnings("unused")
		long start;
		for(int i = 0; i < numRequests; i++){
			start = System.currentTimeMillis();
			peer.lookup(fileName);
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
    		System.out.println("It should be java/client folder port");
    		return;
    	}
    	
    	//Server information
    	String serverAddress = "localhost";
    	int serverPort = 3434;
    	
    	String dir = args[0];
    	File folder = new File(dir);
    	
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
    	
    	String fileName = args[2];
    	
    	sendRequests(peer, fileName, 1000);
    }
}
