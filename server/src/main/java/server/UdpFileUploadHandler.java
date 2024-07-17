package server;

import shared.*;
import shared.Model.User;
import shared.response.TCPUploadResponse;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static server.DataBase.findUser;

public class UdpFileUploadHandler extends Thread {
    private final DatagramSocket socket;
    private final Map<String, FileReceiver> fileReceiverMap = new HashMap<>();
    private final DatagramPacket packet;
    private final int port;
    private final int numClient;
    private volatile boolean running = true;

    public UdpFileUploadHandler(DatagramSocket socket, DatagramPacket packet, int port, int numClient) {
        this.socket = socket;
        this.packet = packet;
        this.port = port;
        this.numClient = numClient;
    }

    @Override
    public void run() {
        try {
            while (running) {
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
            if (running) {
                ex.printStackTrace();
            }
        } finally {
//            socket.close();
//            System.out.println("Socket closed.");
        }
    }

    public void stopHandler() {
        running = false;
        socket.close();
    }

    public static void handleFileUpload(UploadRequest uploadRequest, Map<String, FileReceiver> fileReceiverMap, DatagramSocket socket, InetAddress clientAddress, int numClient) throws IOException {
        String username = uploadRequest.getUsername();
        String token = uploadRequest.getToken();

        boolean isValid = JwtUtil.validateToken(token, username);
        if (!isValid) return;

        String uniqueID = uploadRequest.getUniqueID();
        String path = "./server/DataBase/client"+numClient + "/";
        FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts(), path));

        fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
        System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);

        if (fileReceiver.isComplete()) {
            fileReceiver.finish();
            fileReceiverMap.remove(uniqueID);
            System.out.println("File received and saved as " + fileReceiver.getFileName());

            User user = findUser(username);
            if (user != null) user.addFile(new File(fileReceiver.getFileName()));
        }
    }

    private static void handleFileRequest(FileRequest fileRequest, DatagramSocket socket, InetAddress clientAddress) throws IOException {

        String fileName = fileRequest.getFileName();
        String username = fileRequest.getUsername();

        User user = findUser(username);

        if (user == null) return;

        if (!user.getFileStrings().contains(fileName)) {
            System.err.println("File not found: " + fileName);
            return;
        }

        File file = null;
        for (int i = 0; i < user.getFileStrings().size(); i++) {
            if (user.getFileStrings().get(i).equals(fileName)) {
                file = user.getFiles().get(i);
            }
        }

        if (file == null) return;



//        File file = new File(fileRequest.getFileName());
//        if (!file.exists()) {
//            System.err.println("File not found: " + file.getAbsolutePath());
//            return;
//        }

        String uniqueID = UUID.randomUUID().toString();
        FileInputStream fis = new FileInputStream(file);

        byte[] buffer = new byte[1024];
        int bytesRead;
        int partNumber = 0;
        int totalParts = (int) Math.ceil((double) file.length() /1024);

        while ((bytesRead = fis.read(buffer)) != -1) {
            byte[] dataToSend = new byte[bytesRead];
            System.arraycopy(buffer, 0, dataToSend, 0, bytesRead);

            FileSenderThread fileSenderThread = new FileSenderThread(
                    fileRequest.getUsername(), fileRequest.getToken(), clientAddress,
                    socket, uniqueID, file.getName(), partNumber, dataToSend, totalParts, 101);
            fileSenderThread.start();
            partNumber++;
        }

        fis.close();
//        socket.close();
        System.out.println("File sent to client: " + file.getName());
    }
}
