package bench;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import util.Util;
import client.Peer;

public class Benchmarking {
	
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
    	
    	if(args.length < 4){
    		System.out.println("It should be java bench/Benchmarking folder port fileName numRequests");
    		return;
    	}
    	
    	//Server information
    	String serverAddress = "localhost";
    	int serverPort = 3434;
    	if(args.length > 4){
    		try{
    			serverAddress = args[4];
        		serverPort = Integer.parseInt(args[5]);
    		} catch(Exception e){
    			System.out.println("It should be java bench/Benchmarking folder port fileName numRequests serverAddress serverPort");
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
    	peer.register(new Socket(serverAddress, serverPort));
    	
    	try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    	/*new Thread(){
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
					peer.server();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	}.start();*/
    	
    	String fileName = args[2];
    	
    	int numRequests = 100;
    	
    	try{
    		numRequests = Integer.parseInt(args[3]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	sendRequests(peer, fileName, numRequests);
    }
}
