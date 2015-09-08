package server;

import java.util.ArrayList;

public class Peer {
	private int peerId;
	private int numFiles;
	private ArrayList<String> fileNames;
	private String directory;
	private String address;
	private int port;
	
	public Peer(int peerId, int numFiles, ArrayList<String> fileNames, String directory, String address, int port){
		this.peerId = peerId;
		this.numFiles = numFiles;
		this.fileNames = new ArrayList<String>();
		this.fileNames.addAll(fileNames);
		this.directory = directory;
		this.address = address;
		this.port = port;
	}
	
	//getters
	public int getPeerId(){
		return peerId;
	}
	
	public int getNumFiles(){
		return numFiles;
	}
	
	public ArrayList<String> getFileNames(){
		return fileNames;
	}
	
	public String getDirectory(){
		return directory;
	}
	
	public String getAddress(){
		return address;
	}
	
	public int getPort(){
		return port;
	}
	
	//setters
	public void setPeerId(int peerId){
		this.peerId = peerId;
	}
	
	public void setNumFiles(int numFiles){
		this.numFiles = numFiles;
	}
	
	public void setFileNames(ArrayList<String> fileNames){
		this.fileNames.addAll(fileNames);
	}
	
	public void addFileName(String fileName){
		this.fileNames.add(fileName);
	}
	
	public void setDirectory(String directory){
		this.directory = directory;
	}
	
	public void setAddress(String address){
		this.address = address;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	//methods
	
	public Boolean searchFile(String fileName){
		for(String fn : fileNames){
			if(fn.equals(fileName)){
				return true;
			}
		}
		return false;
	}

}
