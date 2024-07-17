package client;

import shared.FileReceiver;
import shared.UploadRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class FileReceiverHandler extends Thread {
    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int PACKET_SIZE;
    private final String path;

    public FileReceiverHandler(DatagramSocket socket, InetAddress serverAddress, int PACKET_SIZE, String path) {
        this.socket = socket;
        this.serverAddress = serverAddress;
        this.PACKET_SIZE = PACKET_SIZE;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            Map<String, FileReceiver> fileReceiverMap = new HashMap<>();
            byte[] buf = new byte[PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            while (true) {
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                UploadRequest uploadRequest = (UploadRequest) ois.readObject();

                String uniqueID = uploadRequest.getUniqueID();
                FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts(), path));

                fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
                System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);

                if (fileReceiver.isComplete()) {
                    fileReceiver.finish();
                    fileReceiverMap.remove(uniqueID);
                    System.out.println("File received and saved as " + fileReceiver.getFileName());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
