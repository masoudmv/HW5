package client;

import java.io.*;
import java.net.*;
import java.util.*;

import static client.UDPClient.CHUNK_SIZE;
import static client.UDPClient.SERVER_PORT;

public class UDPClient {
    public static final int SERVER_PORT = 8080;
    public static final int CHUNK_SIZE = 1024;

//    public static void main(String[] args) throws Exception {
//        DatagramSocket clientSocket = new DatagramSocket();
//        InetAddress serverAddress = InetAddress.getByName("localhost");
//
//        // Example: Uploading multiple files
//        List<String> filesToUpload = Arrays.asList("C:\\Users\\masoud\\Desktop\\NetworkClass\\client\\src\\file1.txt");
//        for (String filePath : filesToUpload) {
//            new Thread(new FileSender(clientSocket, serverAddress, filePath)).start();
//        }
//    }
}

class FileSender implements Runnable {
    private DatagramSocket clientSocket;
    private InetAddress serverAddress;
    private String filePath;

    public FileSender(DatagramSocket clientSocket, InetAddress serverAddress, String filePath) {
        this.clientSocket = clientSocket;
        this.serverAddress = serverAddress;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            File file = new File(filePath);
            byte[] fileData = new byte[(int) file.length()];
            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileData);
            }

            int numParts = (int) Math.ceil(fileData.length / (double) CHUNK_SIZE);
            String initialMessage = file.getName() + ":" + numParts;
            byte[] initialData = initialMessage.getBytes();
            DatagramPacket initialPacket = new DatagramPacket(initialData, initialData.length, serverAddress, SERVER_PORT);
            clientSocket.send(initialPacket);

            for (int i = 0; i < numParts; i++) {
                int start = i * CHUNK_SIZE;
                int length = Math.min(fileData.length - start, CHUNK_SIZE);
                byte[] partData = Arrays.copyOfRange(fileData, start, start + length);
                DatagramPacket sendPacket = new DatagramPacket(partData, partData.length, serverAddress, SERVER_PORT);
                clientSocket.send(sendPacket);
                System.out.printf("Sending file %s: %d/%d parts sent.%n", file.getName(), i + 1, numParts);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
