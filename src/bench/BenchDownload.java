package bench;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import util.Util;
import client.Peer;

public class BenchDownload{
	
	private static String serverAddress = "localhost";
	private static int serverPort = 3434;
	
	
	public static void sendRequests(Peer peer, String fileName, int numRequests) throws IOException{
		
		long startTime = System.currentTimeMillis();
		
		ArrayList<Long> times = new ArrayList<Long>();
		
		long start;
		
		String peerAddress[] = new String[0];
		
		//long start;
		for(int i = 0; i < numRequests; i++){
			//start = System.currentTimeMillis();
			peerAddress = peer.lookup(fileName, new Socket(serverAddress, serverPort), i);
			if(peerAddress.length > 0){
				String[] addrport = null;
				for(int j = 0; j < peerAddress.length; j++){
					addrport = peerAddress[j].split(":");
					if(addrport[2].equals(Integer.toString(peer.getPeerId()))){
						System.out.println("This peer has the file already, not downloading then.");
					}else{
						//System.out.println("Downloading from peer " + addrport[2] + ": " + addrport[0] + ":" + addrport[1]);
						start = System.currentTimeMillis();
						peer.download(addrport[0], Integer.parseInt(addrport[1]), fileName, i);
						times.add(System.currentTimeMillis() - start);
						break;
					}
				}
			}else {
				System.out.println("Not downloading because file was not found.");
			}
			//System.out.println("Took " + (System.currentTimeMillis() - start) + " ms.");
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
    	final Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
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
