import java.io.*;
import java.net.*;

public class FolderGiver2 {

    public FolderGiver2(int port, String path) {

        Socket user;
        try {

            ServerSocket host = new ServerSocket(port);

            System.out.println("Accepting...");
            user = host.accept();
            System.out.println("Found giving data");

        } catch (IOException e) {

            System.out.println("Cannot host.");
            return;

        }

        Folder folder = new Folder(path);
        getFiles(folder);

        try {

            byte[] data = getBytes(folder);
            //BufferedOutputStream out = new BufferedOutputStream(user.getOutputStream());
            OutputStream out = user.getOutputStream();
            out.write(data);
            out.close();
            //out.flush();
            System.out.println("Sent data.");

            Folder f = (Folder) deserialize(data);
            BufferedReader r = new BufferedReader(new FileReader(f.files.get(0)));
            System.out.println(r.readLine());
            r.close();


        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    }

    private void getFiles(Folder root) {

        File[] filess = root.listFiles();
        for (File fil : filess) {

            if (fil.isDirectory()) {

                Folder folder = new Folder(fil.getAbsolutePath());
                folder.parent = root;
                root.files.add(folder);
                getFiles(folder);

            } else {

                root.files.add(fil);

            }

        }

    }

    private byte[] getBytes(Object obj) {

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream out2 = new ObjectOutputStream(out);
            out2.writeObject(obj);
            byte[] data = out.toByteArray();
            out.close();

            return data;

        } catch (IOException e) {

            e.printStackTrace();
            return new byte[0];

        }

    }

    private Object deserialize(byte[] ary) throws IOException, ClassNotFoundException {

        ByteArrayInputStream in = new ByteArrayInputStream(ary);
        ObjectInputStream objstrm = new ObjectInputStream(in);

        return objstrm.readObject();

    }

    public static void main(String[] args) {

        new FolderGiver2(8000, "H:\\testFolder");

    }

}
