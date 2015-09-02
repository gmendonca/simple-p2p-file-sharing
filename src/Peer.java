import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Peer {
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
}
