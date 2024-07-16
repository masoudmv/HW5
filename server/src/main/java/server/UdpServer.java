package server;

import shared.FileReceiver;
import shared.FileRequest;
import shared.FileSenderThread;
import shared.UploadRequest;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UdpServer {
    public static final int PACKET_SIZE = 2048; // Increased to accommodate serialized objects
    private static final int CLIENT_PORT = 101; // Port to communicate with the client

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(100); // Open datagram socket on port 100

        // Map to hold file data for each unique ID
        Map<String, FileReceiver> fileReceiverMap = new HashMap<>();

        byte[] buf = new byte[PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            socket.receive(packet);
            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object receivedObject = ois.readObject();

            if (receivedObject instanceof UploadRequest) {
                handleFileUpload((UploadRequest) receivedObject, fileReceiverMap, socket, packet.getAddress());
            } else if (receivedObject instanceof FileRequest) {
                handleFileRequest((FileRequest) receivedObject, socket, packet.getAddress());
            }
        }
    }

    public static void handleFileUpload(UploadRequest uploadRequest, Map<String, FileReceiver> fileReceiverMap, DatagramSocket socket, InetAddress clientAddress) throws IOException {
        String uniqueID = uploadRequest.getUniqueID();
        FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts()));

        fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
        System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);

        if (fileReceiver.isComplete()) {
            fileReceiver.finish();
            fileReceiverMap.remove(uniqueID);
            System.out.println("File received and saved as " + fileReceiver.getFileName());
        }
    }

    public static void handleFileRequest(FileRequest fileRequest, DatagramSocket socket, InetAddress clientAddress) throws IOException {
        File file = new File("C:\\Users\\masoud\\Desktop\\dg5an9f-7f40bbe4-28ba-4e3f-8c14-46d948bfb0bc.png");
        if (!file.exists()) {
            System.err.println("File not found: " + file.getAbsolutePath());
            return;
        }

        String uniqueID = UUID.randomUUID().toString();
        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        int bytesRead;
        int partNumber = 0;
        int totalParts = (int) Math.ceil((double) file.length() /1024);

        while ((bytesRead = fis.read(buffer)) != -1) {
            byte[] dataToSend = new byte[bytesRead];
            System.arraycopy(buffer, 0, dataToSend, 0, bytesRead);

            FileSenderThread fileSenderThread = new FileSenderThread(clientAddress, socket, uniqueID, file.getName(), partNumber, dataToSend, totalParts, CLIENT_PORT);
            fileSenderThread.start();
            partNumber++;
        }

        fis.close();
        System.out.println("File sent to client: " + file.getName());
    }
}


