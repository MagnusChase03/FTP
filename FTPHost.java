import java.io.*;
import java.net.*;
import java.util.*;

public class FTPHost {

    private FTPHost() {
    
        upload(getFile(), 8888);
        
    
    }
    
    private File getFile() {
    
        Scanner in = new Scanner(System.in);
        
        System.out.print("Absolute file path: ");
        File file = new File(in.nextLine());
        
        in.close();       
        return file;
    
    }
    
    private void upload(File file, int port) {
        
        try {
        
            ServerSocket server = new ServerSocket(port);    
            Socket client = server.accept();
            
            BufferedOutputStream out = new 
                BufferedOutputStream(client.getOutputStream());
                
            out.write(toBytes(file));
            out.close();
        
        } catch (IOException e) {
        
            e.printStackTrace();
            return;
        
        }
    
    }
    
    private byte[] toBytes(File file) {
    
        try {
        
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream out2 = new ObjectOutputStream(out);
            out2.writeObject(file);
            return out.toByteArray();
            
        } catch (IOException e) {
        
            e.printStackTrace();
            return new byte[0];
        
        }
    
    }

    public static void main(String[] args) {
    
        new FTPHost();
    
    }

}
