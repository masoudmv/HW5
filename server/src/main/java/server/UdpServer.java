package server;

import shared.FileReceiver;
import shared.request.UploadRequest;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UdpServer {
    private static final int PACKET_SIZE = 2048; // Increased to accommodate serialized objects

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(8080);

        Map<String, FileReceiver> fileReceiverMap = new HashMap<>();

        byte[] buf = new byte[PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            socket.receive(packet);
            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            ObjectInputStream ois = new ObjectInputStream(bais);
            UploadRequest uploadRequest = (UploadRequest) ois.readObject();

            String uniqueID = uploadRequest.getUniqueID();
            if (uploadRequest.isLastPacket()) {
                FileReceiver fileReceiver = fileReceiverMap.get(uniqueID);
                if (fileReceiver != null) {
                    fileReceiver.finish();
                    fileReceiverMap.remove(uniqueID);
                    System.out.println("File received and saved as " + fileReceiver.getFileName());
                }
            } else {
                FileReceiver fileReceiver = fileReceiverMap.get(uniqueID);
                if (fileReceiver == null) {
                    fileReceiver = new FileReceiver(uploadRequest.getFileName());
                    fileReceiverMap.put(uniqueID, fileReceiver);
                    System.out.println("Initialized file reception: " + uploadRequest.getFileName() + " with ID: " + uniqueID);
                }
                fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
                System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);
            }
        }
    }
}
