package server;

import java.io.*;
import java.net.*;

import static server.UDPServer.OUTPUT_DIR;

public class UDPServer {
    public static final int PORT = 8080;
    public static final String OUTPUT_DIR = "C:\\Users\\masoud\\Desktop\\NetworkClass\\server\\received_files";

//    public static void main(String[] args) throws Exception {
//        DatagramSocket serverSocket = new DatagramSocket(PORT);
//        byte[] receiveData = new byte[1024];
//
//        while (true) {
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            serverSocket.receive(receivePacket);
//            new Thread(new FileReceiver(serverSocket, receivePacket)).start();
//        }
//    }
}

class FileReceiver implements Runnable {
    private DatagramSocket serverSocket;
    private DatagramPacket initialPacket;

    public FileReceiver(DatagramSocket serverSocket, DatagramPacket initialPacket) {
        this.serverSocket = serverSocket;
        this.initialPacket = initialPacket;
    }

    @Override
    public void run() {
        try {
            InetAddress clientAddress = initialPacket.getAddress();
            int clientPort = initialPacket.getPort();
            String initialMessage = new String(initialPacket.getData(), 0, initialPacket.getLength()).trim();

            System.out.println(initialMessage);
            System.out.println("======");

            String[] parts = initialMessage.split(":");
            if (parts.length < 2) {
                System.err.println("Invalid initial message format: " + initialMessage);
                return;
            }
            String fileName = parts[0];
            int numParts = Integer.parseInt(parts[1]);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] receiveData = new byte[1024];
            for (int i = 0; i < numParts; i++) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                baos.write(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.printf("Receiving file %s: %d/%d parts received.%n", fileName, i + 1, numParts);
            }

            File outputFile = new File(OUTPUT_DIR + fileName);
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(baos.toByteArray());
            }

            System.out.printf("File %s received and saved.%n", fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
