package bench;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import util.Util;
import client.Peer;

public class BenchRegistry {
	
	public static ArrayList<Long> times;

	public static void main(String[] args) throws IOException {
		
		times = new ArrayList<Long>();
		
		int numThreads = 4;
    	
    	if(args.length < 3){
    		System.out.println("It should be java bench/BenchRegistry folder port numPeers");
    		return;
    	}
    	
    	//Server information
    	String serverAddress = "localhost";
    	int serverPort = 3434;
    	if(args.length > 4){
    		try{
    			serverAddress = args[3];
        		serverPort = Integer.parseInt(args[4]);
    		} catch(Exception e){
    			System.out.println("It should be java bench/BenchRegistry folder port numPeers serverAddress serverPort");
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
    	int port = 13000;
    	try{
    		port = Integer.parseInt(args[1]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
    	
    	int numPeers = 10;
    	
    	try{
    		numPeers = Integer.parseInt(args[2]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	ExecutorService executor = Executors.newFixedThreadPool(numThreads);
    	
    	Socket socket = null;
    	
    	long start = System.currentTimeMillis();
		for(int i = 0; i < numPeers; i++){
			try {
	    		socket = new Socket(serverAddress, serverPort);
	    	}catch (IOException e){
	    		System.out.println("There isn't any instance of server running. Start one first!");
	    		return;
	    	}
			RegistryThread rt = new RegistryThread(new Peer(dir, fileNames, fileNames.size(), address, port + i), socket);
			executor.execute(rt);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			System.out.println("Couldn't wait for the tasks to fullfil!");
		}
		System.out.println("Average of Peer registry of "+ numPeers + " operations is " + Util.calculateAverage(times) + " ms.");
		System.out.println("Overall time = " + (System.currentTimeMillis() - start) + " ms");
    }
}
