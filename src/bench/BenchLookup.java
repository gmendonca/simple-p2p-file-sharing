package bench;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import util.Util;
import client.Peer;

public class BenchLookup {
	
	private static String serverAddress = "localhost";
	private static int serverPort = 3434;
	
	
	//public static void sendRequests(Peer peer, ArrayList<String> fileNames, int numRequests) throws IOException{
	public static void sendRequests(Peer peer, String fileName, int numRequests) throws IOException{
		
		long startTime = System.currentTimeMillis();
		
		ArrayList<Long> times = new ArrayList<Long>();
		
		long start;
		int count = 0;
		for(int i = 0; i < numRequests; i++){
			start = System.currentTimeMillis();
			Socket socket = new Socket(serverAddress,serverPort);
			//peer.lookup(fileNames.get(count++), socket, i);
			peer.lookup(fileName, socket, i);
			if(count == 10) count = 0;
			times.add(System.currentTimeMillis() - start);
		}
		
		long stopTime = System.currentTimeMillis();
		
		//System.out.println("Overall -> Took " + Util.toSeconds(startTime, stopTime) + " s.");
		System.out.println("==============================================================================================");
		System.out.println("Average of Peer " + peer.getPeerId() + "'s "+ numRequests + " operations is " + Util.calculateAverage(times) + " ms.");
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
    	Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
    	Socket socket = null;
    	try {
    		socket = new Socket(serverAddress, serverPort);
    	}catch (IOException e){
    		System.out.println("There isn't any instance of server running. Start one first!");
    		return;
    	}
    	peer.register(socket);
    	
    	try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	String fileName = args[2];
    	//File folderLookup = new File(fileName);
    	//ArrayList<String> fileNamesLookup = Util.listFilesForFolder(folderLookup);
    	
    	int numRequests = 100;
    	
    	try{
    		numRequests = Integer.parseInt(args[3]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	//sendRequests(peer, fileNamesLookup, numRequests);
    	sendRequests(peer, fileName, numRequests);
    }
}
