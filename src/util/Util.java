package util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class Util {
	
	public static ArrayList<String> listFilesForFolder(final File folder) {
		
		ArrayList<String> fileNames = new ArrayList<String>();
	    
		for (final File fileEntry : folder.listFiles()) 
	    	fileNames.add(fileEntry.getName());
		
		return fileNames;

	}
	
	public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = in.read(buffer)) != -1) {
            out.write(buffer, 0, count);
        }
    }
	
	public static long toSeconds(long start, long end){
		return (end-start)/1000;
	}
}
