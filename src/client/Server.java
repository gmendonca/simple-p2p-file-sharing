package client;

import java.io.DataInputStream;
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
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
	        Socket socket = serverSocket.accept();
	        DataInputStream dIn = new DataInputStream(socket.getInputStream());
	        String fileName = dIn.readUTF();
	        InputStream in = new FileInputStream(fileName);
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
