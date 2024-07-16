package server;

import shared.FileReceiver;
import shared.FileRequest;
import shared.FileSenderThread;
import shared.UploadRequest;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class UdpFileUploadHandler extends Thread {
    DatagramSocket socket;
    private final Map<String, FileReceiver> fileReceiverMap = new HashMap<>();
    DatagramPacket packet;
    int port;
    int numClient;

    public UdpFileUploadHandler(DatagramSocket socket, DatagramPacket packet, int port, int numClient) {
        this.socket = socket;
        this.packet = packet;
        this.port = port;
        this.numClient = numClient;
    }

    @Override
    public void run() {
        try {

            while (true) {
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object receivedObject = ois.readObject();

                if (receivedObject instanceof UploadRequest) {
                    handleFileUpload((UploadRequest) receivedObject, fileReceiverMap, socket, packet.getAddress(), numClient);
                } else if (receivedObject instanceof FileRequest) {
                    handleFileRequest((FileRequest) receivedObject, socket, packet.getAddress());
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void handleFileUpload(UploadRequest uploadRequest, Map<String, FileReceiver> fileReceiverMap, DatagramSocket socket, InetAddress clientAddress, int numClient) throws IOException {
        String uniqueID = uploadRequest.getUniqueID();
        String path = "./server/DataBase/client"+numClient + "/";
        FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts(), path));

        fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
        System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);

        if (fileReceiver.isComplete()) {
            fileReceiver.finish();
            fileReceiverMap.remove(uniqueID);
            System.out.println("File received and saved as " + fileReceiver.getFileName());
        }
    }

    private static void handleFileRequest(FileRequest fileRequest, DatagramSocket socket, InetAddress clientAddress) throws IOException {
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

            FileSenderThread fileSenderThread = new FileSenderThread(clientAddress, socket, uniqueID, file.getName(), partNumber, dataToSend, totalParts, 101);
            fileSenderThread.start();
            partNumber++;
        }

        fis.close();
        System.out.println("File sent to client: " + file.getName());
    }
}
