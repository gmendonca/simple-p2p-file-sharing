import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class CentralIndexingServer {
	
	private static HashMap index;
	
	private static void server() throws IOException{
		ServerSocket serverSocket = new ServerSocket(2386);
		Socket socket = serverSocket.accept();
	}
	
	public static void register(){
		
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
