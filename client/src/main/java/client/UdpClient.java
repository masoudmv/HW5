//package client;
//
//import java.io.File;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.Arrays;
//import java.util.List;
//
//public class UdpClient {
//    public static final int PACKET_SIZE = 1024;
//    private static final int PORT = 8080;
//
//    public static void main(String[] args) throws Exception {
//        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
//        DatagramSocket socket = new DatagramSocket();
//
//        // List of files to be sent
//        List<File> filesToSend = Arrays.asList(
//                new File("C:\\Users\\masoud\\Desktop\\file1.txt"),
//                new File("C:\\Users\\masoud\\Desktop\\file2.txt")
//        );
//
//        // Create and start a FileUploadManager for each file
//        for (File file : filesToSend) {
//            shared.FileUploadManager uploadManager = new shared.FileUploadManager(serverAddress, socket, file, PORT);
//            uploadManager.start();
//        }
//    }
//}
