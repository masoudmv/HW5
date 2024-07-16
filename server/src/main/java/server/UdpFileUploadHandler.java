package server;

import shared.FileReceiver;
//import shared.UploadRequest;
import shared.FileRequest;
import shared.UploadRequest;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import static server.UdpServer.handleFileRequest;
import static server.UdpServer.handleFileUpload;

public class UdpFileUploadHandler extends Thread {
    DatagramSocket socket;
    private final Map<String, FileReceiver> fileReceiverMap = new HashMap<>();
    DatagramPacket packet;
    int port;

    public UdpFileUploadHandler(DatagramSocket socket, DatagramPacket packet, int port) {
        this.socket = socket;
        this.packet = packet;
        this.port = port;
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
                    handleFileUpload((UploadRequest) receivedObject, fileReceiverMap, socket, packet.getAddress());
                } else if (receivedObject instanceof FileRequest) {
                    handleFileRequest((FileRequest) receivedObject, socket, packet.getAddress());
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

//    private void handleFileUpload(UploadRequest uploadRequest, Map<String, FileReceiver> fileReceiverMap, DatagramSocket socket, InetAddress clientAddress) throws IOException {
//        String uniqueID = uploadRequest.getUniqueID();
//        FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts()));
//
//        fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
//        System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);
//
//        if (fileReceiver.isComplete()) {
//            fileReceiver.finish();
//            fileReceiverMap.remove(uniqueID);
//            System.out.println("File received and saved as " + fileReceiver.getFileName());
//        }
//    }
}
