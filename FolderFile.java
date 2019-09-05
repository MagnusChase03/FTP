import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FolderFile extends File {

    public String contents = "";

    public FolderFile(String path) {

        super(path);

        try {

            BufferedReader in = new BufferedReader(new FileReader(this));
            String line = "";
            while ((line = in.readLine()) != null) {

                contents += line;

            }
            in.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
