package server;

import shared.FileReceiver;
//import shared.UploadRequest;
import shared.request.UploadRequest;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UdpFileUploadHandler extends Thread {
    private final int port;
    private final Map<String, FileReceiver> fileReceiverMap = new HashMap<>();

    public UdpFileUploadHandler(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buf = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            while (true) {
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Object receivedObject = ois.readObject();

                if (receivedObject instanceof UploadRequest) {
                    handleFileUpload((UploadRequest) receivedObject, fileReceiverMap, socket, packet.getAddress());
                }
                // Add more conditions if there are other types of requests.
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleFileUpload(UploadRequest uploadRequest, Map<String, FileReceiver> fileReceiverMap, DatagramSocket socket, InetAddress clientAddress) throws IOException {
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
}
