import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class FolderReciever {

	public FolderReciever(String host, int port, String originalPath) throws IOException, ClassNotFoundException, InterruptedException {

        Socket user = new Socket(host, port);
        //System.out.println(user.getInputStream().available());
        
        Thread.sleep(1000);
        
        InputStream in = user.getInputStream();
        //System.out.println(in.available());
        byte[] inBytes = new byte[1024000];
        int index = 0;
        
        while (in.available() > 0) {
        	
        	inBytes[index] = (byte) in.read();
        	//System.out.println(inBytes[index]);
        	index++;
        	
        }
        
        Folder folder = (Folder) deserialize(inBytes);
        folder.mkdirs();
        process(folder);
        
        in.close();
        user.close();

    }
	
	public static void process(Folder fol) throws IOException{
		
		for(File f : fol.files) {
			
			if(f.isDirectory()) {
				
				//System.out.println("jjkjhgfh");
				f.mkdir();
				process((Folder)f);
				
			} else {
				
				
				String filename = f.getAbsolutePath();
				new File(filename).createNewFile();
				
				BufferedReader in = new BufferedReader(new FileReader(f));
				String line = "";
				String contents = "";
				while ((line = in.readLine()) != null) {

					//System.out.println(line);
		            contents += line;
		            contents += "\n";

		        }
		        BufferedWriter write = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
		        write.write(contents);
				
			}
			
		}
		System.out.println(fol.files.get(0));
		
	}
	
	private Object deserialize(byte[] ary) throws IOException, ClassNotFoundException {
		
		ByteArrayInputStream in = new ByteArrayInputStream(ary);
		ObjectInputStream objstrm = new ObjectInputStream(in);
		
		return objstrm.readObject();
		
	}

    public static void main(String[] args) throws Exception {

        new FolderReciever("10.8.5.78", 8000, "H:\\");

    }
	
}

class Folder extends File {
	
	
	public Folder parent;
	public ArrayList<File> files = new ArrayList<>();
	
	public Folder(String filepath) {
		
		super(filepath);
		
	}
	
}

