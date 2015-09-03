import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class CentralIndexingServer {
	
	private static HashMap index;
	
	private static int id = 0;
	
	private static int getUniqueId(){
		return id++;
	}
	
	private static void server() throws IOException{
		ServerSocket serverSocket = new ServerSocket(2386);
		Socket socket = serverSocket.accept();
		
		int peerId = getUniqueId();
		
		DataInputStream dIn = new DataInputStream(socket.getInputStream());
		
		Boolean end = false;
		String directory = "";
		ArrayList<String> fileNames = new ArrayList<String>();
		int numFiles = 0;
		
		
		while(!end){
			byte messageType = dIn.readByte();
			
			 switch(messageType){
			 	case 1:
			 		directory = dIn.readUTF();
			 		break;
			 	case 2:
			 		numFiles = dIn.readInt();
			 		break;
			 	case 3:
			 		for(int i = 0; i < numFiles; i++){
			 			fileNames.add(dIn.readUTF());
			 		}
			 		break;
			 	default:
			 		end = true;
			 }
		}
		
		register(peerId, directory, fileNames, numFiles);
		
		DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
		dOut.writeInt(peerId);
		dOut.flush();
		dOut.close();
		
	}
	
	public static void register(int peerId, String directory, ArrayList<String> fileNames, int numFiles){
		
	}
	
	public static void search(){
		
	}
	
	public static void main(String[] args) throws IOException {
		
		index = new HashMap();
		
		new Thread() {
            public void run() {
                try {
                   server(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
	}
}
