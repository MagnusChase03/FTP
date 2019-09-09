import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class FolderGiver extends JFrame {

    private Socket user;
    private ServerSocket server;
    private JTextArea port;
    private JTextArea path;

    public FolderGiver() {

        createGUI();

    }

    private void createGUI() {

        setTitle("Folder Giver");
        setSize(720, 330);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        port = new JTextArea();
        port.setBounds(250, 50, 200, 30);
        port.setFont(new Font("monospace", Font.PLAIN, 20));
        port.setMargin(new Insets(0, 10, 0, 0));
        port.setText("Port number");
        panel.add(port);

        path = new JTextArea();
        path.setBounds(250, 110, 200, 30);
        path.setFont(new Font("monospace", Font.PLAIN, 20));
        path.setMargin(new Insets(0, 10, 0, 0));
        path.setText("Folder path");
        panel.add(path);

        JButton send = new JButton("Send");
        send.setBounds(250, 180, 200, 30);
        send.setFont(new Font("monospace", Font.PLAIN, 20));
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                createServer();
                sendData();

            }
        });
        panel.add(send);

        setVisible(true);

    }

    private void createServer() {

        try {

            server = new ServerSocket(Integer.parseInt(port.getText()));
            user = server.accept();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void sendData() {

        Folder folder = new Folder(path.getText());
        getFiles(folder);

        try {

            byte[] data = getBytes(folder);
            OutputStream out = user.getOutputStream();
            out.write(data);
            out.close();
            server.close();

        } catch (IOException e) {

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

                FolderFile ff = new FolderFile(fil.getAbsolutePath());
                root.files.add(ff);

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

    public static void main(String[] args) {

        new FolderGiver();

    }

}
