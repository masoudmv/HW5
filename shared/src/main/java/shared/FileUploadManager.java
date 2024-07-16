package shared;

import java.io.*;
import java.net.*;
import java.util.UUID;

public class FileUploadManager extends Thread {
    private InetAddress serverAddress;
    private DatagramSocket socket;
    private File file;
    private int port;

    public FileUploadManager(InetAddress serverAddress, DatagramSocket socket, File file, int port) {
        this.serverAddress = serverAddress;
        this.socket = socket;
        this.file = file;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            String fileName = file.getName();
            String uniqueID = UUID.randomUUID().toString();
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            int partNumber = 0;
            int totalParts = (int) Math.ceil((double) file.length() / 1024);

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] dataToSend = new byte[bytesRead];
                System.arraycopy(buffer, 0, dataToSend, 0, bytesRead);

                FileSenderThread fileSenderThread = new FileSenderThread(serverAddress, socket, uniqueID, fileName, partNumber, dataToSend, totalParts, port);
                fileSenderThread.start();
                partNumber++;
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
