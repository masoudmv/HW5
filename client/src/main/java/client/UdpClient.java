//package client;
//
//import shared.FileReceiver;
//import shared.FileRequest;
////import shared.FileUploadManager;
//import shared.request.UploadRequest;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.ObjectInputStream;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class UdpClient {
//    public static final int PACKET_SIZE = 1024;
//    private static final int SERVER_PORT = 8080;
//    private static final int CLIENT_PORT = 8081; // Client port to receive files
//
//    public static void main(String[] args) throws Exception {
//        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
//        DatagramSocket socket = new DatagramSocket(CLIENT_PORT);
//
//        // List of files to be sent
//        List<File> filesToSend = Arrays.asList(
//                new File("C:\\Users\\masoud\\Desktop\\file1.txt"),
//                new File("C:\\Users\\masoud\\Desktop\\dg5an9f-7f40bbe4-28ba-4e3f-8c14-46d948bfb0bc.png")
//        );
//
//        // Send files to the server
//        for (File file : filesToSend) {
//            FileUploadManager uploadManager = new FileUploadManager(serverAddress, socket, file, SERVER_PORT);
//            uploadManager.start();
//        }
//
//        // Map to hold file data for each unique ID
//        Map<String, FileReceiver> fileReceiverMap = new HashMap<>();
//
//        byte[] buf = new byte[PACKET_SIZE];
//        DatagramPacket packet = new DatagramPacket(buf, buf.length);
//
//        while (true) {
//            socket.receive(packet);
//            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            UploadRequest uploadRequest = (UploadRequest) ois.readObject();
//
//            String uniqueID = uploadRequest.getUniqueID();
//            FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts()));
//
//            fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
//            System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);
//
//            if (fileReceiver.isComplete()) {
//                fileReceiver.finish();
//                fileReceiverMap.remove(uniqueID);
//                System.out.println("File received and saved as " + fileReceiver.getFileName());
//            }
//        }
//    }
//}
