import java.io.*;

public class FolderFile extends File {

    public byte[] contents;

    public FolderFile(String path) {

        super(path);

        contents = new byte[(int) this.length()];
        try {

            FileInputStream in = new FileInputStream(this);
            in.read(contents);
            in.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
