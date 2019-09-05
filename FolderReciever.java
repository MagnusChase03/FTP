import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FolderReciever {

	public FolderReciever(String host, int port, String originalPath) throws IOException, ClassNotFoundException, InterruptedException {

        Socket user = new Socket(host, port);

        //delay to process
        Thread.sleep(5000);
        
        InputStream in = user.getInputStream();
        byte[] inBytes = new byte[2000000000];
        int index = 0;
        
        while (in.available() > 0) {
        	
        	inBytes[index] = (byte) in.read();
        	index++;
        	
        }
        
        Folder folder = (Folder) deserialize(inBytes);
        folder.mkdirs();
        process(folder);
            
        in.close();
        user.close();

    }
	
	public static void process(Folder fol) throws IOException {
	
		for(File f : fol.files) {
			
			
			if(f.getName().indexOf(".") < 0) {
				
				Files.createDirectories(Paths.get(f.getAbsolutePath()));
				process((Folder)f);
				
			} else if(f instanceof FolderFile){
				
				
		        BufferedWriter write = new BufferedWriter(new FileWriter(f.getAbsoluteFile()), 1);
		        write.write(((FolderFile)f).contents);
				write.close();
		        
			}
			
		}
		//System.out.println(fol.files.get(0));
		
	}
	
	private Object deserialize(byte[] ary) throws IOException, ClassNotFoundException {
		
		ByteArrayInputStream in = new ByteArrayInputStream(ary);
		ObjectInputStream objstrm = new ObjectInputStream(in);
		
		return objstrm.readObject();
		
	}

    public static void main(String[] args) throws Exception {

        new FolderReciever("10.8.5.75", 8000, "H:\\");

    }
	
}



