import java.io.File;
import java.util.ArrayList;

public class Folder extends File {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3346849220365395213L;
	/**
	 * 
	 */
	
	public Folder parent;
	public ArrayList<File> files = new ArrayList<>();
	
	public Folder(String filepath) {
		
		super(filepath);
		
	}
	
}