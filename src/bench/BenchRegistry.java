package bench;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import util.Util;
import client.Peer;

public class BenchRegistry {

	public static void main(String[] args) throws IOException {
		
		int numThreads = 4;
    	
    	if(args.length < 4){
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
    	//Peer peer = new Peer(dir, fileNames, fileNames.size(), address, port);
    	//peer.register(new Socket(serverAddress, serverPort));
    	
    	int numPeers = 10;
    	
    	try{
    		numPeers = Integer.parseInt(args[2]);
    	} catch (Exception e){
    		System.out.println("Put a valid port number");
    	}
    	
    	ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		for(int i = 0; i < numPeers; i++){
			RegistryThread rt = new RegistryThread(new Peer(dir, fileNames, fileNames.size(), address, port + i), serverAddress, serverPort);
			executor.execute(rt);
		}
    }
}
