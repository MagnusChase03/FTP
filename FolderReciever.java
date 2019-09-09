import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;


public class FolderReciever extends JFrame {
	
	private Socket user;
	private String host;
	private int port;
	private String originalPath;
	
	private File saveFile = new File("H:\\schoolHDrive\\Fun\\src\\Saves.txt");
	
	private JTextField inPath = new JTextField("Enter The Recieving File Location (path)");
	private JTextField inIpAddress = new JTextField("Enter The Sender IP Address");
	private JTextField inPort = new JTextField("Enter The Port");
	private JButton recieve = new JButton("RECIEVE");
	
	private JComboBox<RecieveSession> saves = new JComboBox<RecieveSession>();
	
	/**
	 * @param host
	 * @param port
	 * @param originalPath
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public FolderReciever() throws IOException, ClassNotFoundException, InterruptedException {
		
		setup();
		
    }
	
	private void setup() {
		
		setSize(720, 710);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setLayout(null);

		inPath.setBounds(10, 10, 690, 70);
		inPath.setFont(new Font("Monospace", Font.PLAIN, 20));
		add(inPath);
		
		inIpAddress.setBounds(10, 100, 690, 70);
		inIpAddress.setFont(new Font("Monospace", Font.PLAIN, 20));
		add(inIpAddress);
		
		inPort.setBounds(10, 190, 690, 70);
		inPort.setFont(new Font("Monospace", Font.PLAIN, 20));
		add(inPort);
		
		recieve.setBounds(310, 300, 100, 80);
		recieve.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					recieveFiles();
				} catch (ClassNotFoundException | InterruptedException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		add(recieve);
		
		saves.setBounds(10, 400, 690, 50);
		saves.setFont(new Font("Monospace", Font.PLAIN, 20));
		
		saves.addItem(null);
		try {
			readInSaves();
		} catch (IOException e1) {	}
		
		saves.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSelectedItem();
			}
		});
		
		add(saves);

	}
	
	private void readInSaves() throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(saveFile));
		String line = "";
		while((line = br.readLine()) != null) {
			
			String[] session = line.split(" ");
			saves.addItem(new RecieveSession(session[0], Integer.parseInt(session[1]), session[2]));

		}
		
		br.close();
		
	}
	
	public void updateSelectedItem() {
		
		RecieveSession item;
		if((item = (RecieveSession) saves.getSelectedItem()) != null) {
			
			inPath.setText(item.path);
			inIpAddress.setText(item.ipAddress);
			inPort.setText("" + item.port);
			
		}
		
	}
	
	public void recieveFiles() throws InterruptedException, IOException, ClassNotFoundException {

		originalPath = inPath.getText();
		host = inIpAddress.getText();
		port = Integer.parseInt(inPort.getText());
		
		user = new Socket(host, port);
		
		Thread.sleep(500);
        
        InputStream in = user.getInputStream();
        byte[] inBytes = new byte[2000000000];
        int index = 0;
        
        while (in.available() > 0) {
        	
        	inBytes[index] = (byte) in.read();
        	index++;
        	
        }
        
        Folder folder = (Folder) deserialize(inBytes);
		Files.createDirectories(Paths.get(originalPath));
        process(folder, originalPath);
            
        in.close();
        user.close();
		
	}
	
	public static void process(Folder fol, String originalPath) throws IOException {
	
		for(File f : fol.files) {
			
			String filepath = originalPath + f.getAbsolutePath().substring(f.getAbsolutePath().indexOf("\\"));
			System.out.println(filepath);
			if(f.getName().indexOf(".") < 0) {
				
				Files.createDirectories(Paths.get(filepath));
				process((Folder)f, originalPath);
				
			} else if(f instanceof FolderFile){
				
				FileOutputStream fos = new FileOutputStream(new File(filepath));
				fos.write(((FolderFile)f).contents);
		      		        
			}
			
		}
		
	}
	
	private Object deserialize(byte[] ary) throws IOException, ClassNotFoundException {
		
		ByteArrayInputStream in = new ByteArrayInputStream(ary);
		ObjectInputStream objstrm = new ObjectInputStream(in);
		
		return objstrm.readObject();
		
	}

    public static void main(String[] args) throws Exception {

        new FolderReciever();

    }
	
}



