package shared;

import shared.request.UploadRequest;
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

            byte[] buffer = new byte[1024]; /// :)))
            int bytesRead;
            int partNumber = 0;

            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] dataToSend = new byte[bytesRead];
                System.arraycopy(buffer, 0, dataToSend, 0, bytesRead);

                FileSenderThread senderThread = new FileSenderThread(serverAddress, socket, uniqueID, fileName, partNumber, dataToSend, port);
                senderThread.start();
                partNumber++;

                // Ensure that senderThread finishes before continuing
                senderThread.join();
            }

            // Send end of file signal
            UploadRequest endRequest = new UploadRequest(uniqueID, fileName, partNumber, new byte[0], true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(endRequest);
            oos.flush();
            byte[] endSignal = baos.toByteArray();

            DatagramPacket endPacket = new DatagramPacket(endSignal, endSignal.length, serverAddress, port);
            socket.send(endPacket);
            System.out.println("Sent end of file signal for: " + fileName);

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
