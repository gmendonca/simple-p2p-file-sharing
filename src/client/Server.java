package client;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import util.Util;

public class Server extends Thread{
	
	private String directory;
	private Socket socket;
	
	public Server(Socket socket, String directory){
		this.directory = directory;
		this.socket = socket;
	}
	
	public void run(){
		try{
	        DataInputStream dIn = new DataInputStream(socket.getInputStream());
	        String fileName = dIn.readUTF();
	        InputStream in = new FileInputStream(directory + "/" + fileName);
	        OutputStream out = socket.getOutputStream();
	        Util.copy(in, out);
	        dIn.close();
	        out.close();
	        in.close();
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
		
	}
}
