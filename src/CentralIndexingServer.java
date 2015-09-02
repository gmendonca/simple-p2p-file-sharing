import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;


public class CentralIndexingServer {
	
	private static HashMap index;
	
	private static void server() throws IOException{
		ServerSocket server = new ServerSocket(2386);
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
