package client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import util.Util;

public class Server extends Thread{
	
	private int port;
	
	public Server(int port, String directory){
		this.port = port;
	}
	
	public void run(){
		try{
			ServerSocket serverSocket = new ServerSocket(3434);
	        Socket socket = serverSocket.accept();
	        InputStream in = new FileInputStream("test1.txt");
	        OutputStream out = socket.getOutputStream();
	        Util.copy(in, out);
	        out.close();
	        in.close();
		}catch (IOException ioe){
			ioe.printStackTrace();
		}
		
	}
}
