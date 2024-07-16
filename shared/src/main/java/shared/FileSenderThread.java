package shared;

//import shared.UploadRequest;

import shared.request.UploadRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class FileSenderThread extends Thread {
    private InetAddress serverAddress;
    private DatagramSocket socket;
    private String uniqueID;
    private String fileName;
    private int partNumber;
    private byte[] fileData;
    private int totalParts;
    private int port;

    public FileSenderThread(InetAddress serverAddress, DatagramSocket socket, String uniqueID, String fileName, int partNumber, byte[] fileData, int totalParts, int port) {
        this.serverAddress = serverAddress;
        this.socket = socket;
        this.uniqueID = uniqueID;
        this.fileName = fileName;
        this.partNumber = partNumber;
        this.fileData = fileData;
        this.totalParts = totalParts;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            UploadRequest request = new UploadRequest(uniqueID, fileName, partNumber, fileData, totalParts);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();
            byte[] dataToSend = baos.toByteArray();

            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, serverAddress, port);
            socket.send(packet);
            System.out.println("Sent part " + partNumber + " of file " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
