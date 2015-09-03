import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Peer {
	
	private int peerId;
	private String directory;
	private ArrayList<String> fileNames;
	private int numFiles;
	
	public Peer(String directory, ArrayList<String> fileNames, int numFiles){
		this.directory = directory;
		this.fileNames = fileNames;
		this.numFiles = numFiles;
		
	}
	
	public void server() throws IOException {
        ServerSocket serverSocket = new ServerSocket(3434);
        Socket socket = serverSocket.accept();
        InputStream in = new FileInputStream("test1.txt");
        OutputStream out = socket.getOutputStream();
        copy(in, out);
        out.close();
        in.close();
    }

    public void client() throws IOException {
        Socket socket = new Socket("localhost", 3434);
        InputStream in = socket.getInputStream();
        OutputStream out = new FileOutputStream("test2.txt");
        copy(in, out);
        out.close();
        in.close();
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
    }
    
    private void register() throws IOException {
    	Socket socket = new Socket("localhost", 2386);
    	DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
    	
    	//Name of directory
    	dOut.writeByte(1);
    	dOut.writeUTF(directory);
    	dOut.flush(); 
    	//Number of files
    	dOut.writeByte(2);
    	dOut.writeInt(numFiles);
    	dOut.flush(); 
    	//Files names
    	dOut.writeByte(3);
    	for(String str : fileNames)
    		dOut.writeUTF(str);
    	dOut.flush();
    	dOut.writeByte(-1);
    	dOut.flush();
    	dOut.close();
    	
    	//Reading the Unique Id from the Server
    	DataInputStream dIn = new DataInputStream(socket.getInputStream());
    	this.peerId = dIn.readInt();
    	dIn.close();
    	
    	System.out.println("Running as Peer " + peerId + "!");
	}

    
    public static void main(String[] args) throws IOException {
    	
    	String dir = args[0];
    	File folder = new File(dir);
    	
    	if(!folder.isDirectory()){
			System.out.println("Put a valid directory name");
			return;
    	}
    	
    	ArrayList<String> fileNames = Util.listFilesForFolder(folder);
    	Peer peer = new Peer(dir, fileNames, fileNames.size());
    	peer.register();
    }
}

	
