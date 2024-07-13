package client;//package client;
//
//
//import client.socket.SocketRequestSender;
//import shared.request.HiRequest;
//import shared.request.LoginRequest;
//import shared.request.SignInRequest;
//import shared.response.HiResponse;
//import shared.response.Response;
//import shared.response.ResponseHandler;
//import shared.response.SignInResponse;
//
//import java.io.IOException;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) throws IOException {
////        SocketRequestSender socketRequestSender = new SocketRequestSender();
////        ServerHandler serverHandler = new ServerHandler(socketRequestSender);
////        socketRequestSender.sendRequest(new SignInRequest("a", "1")).run(serverHandler);
////        socketRequestSender.sendRequest(new LoginRequest("a", "1")).run(serverHandler);
////        Scanner scanner = new Scanner(System.in);
////        scanner.nextLine();
//
//
//
//
//
//        DatagramSocket clientSocket = new DatagramSocket();
//        InetAddress serverAddress = InetAddress.getByName("localhost");
//
//        // Example: Uploading multiple files
//        List<String> filesToUpload = Arrays.asList("C:\\Users\\masoud\\Desktop\\NetworkClass\\client\\src\\file1.txt",
//                "C:\\Users\\masoud\\Desktop\\NetworkClass\\client\\src\\file2.txt");
//        for (String filePath : filesToUpload) {
//            new Thread(new FileSender(clientSocket, serverAddress, filePath)).start();
//        }
//
//    }
//
//
//
//}






import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpFileUploader {
    private static final int CHUNK_SIZE = 10; // Size of each chunk
    private static final int PORT = 8080; // Server port

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\masoud\\Desktop\\NetworkClass\\client\\src\\file1.txt");
        FileInputStream fis = new FileInputStream(file);
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
        long fileSize = file.length();
        int totalChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);

        for (int i = 0; i < totalChunks; i++) {
            int chunkSize = (i == totalChunks - 1) ? (int) (fileSize - (i * CHUNK_SIZE)) : CHUNK_SIZE;
            byte[] buffer = new byte[chunkSize];
            fis.read(buffer, 0, chunkSize);

            Thread thread = new Thread(new UdpSender(buffer, serverAddress, PORT, i));
            thread.start();
        }

        fis.close();
    }
}

class UdpSender implements Runnable {
    private byte[] buffer;
    private InetAddress serverAddress;
    private int port;
    private int chunkId;

    public UdpSender(byte[] buffer, InetAddress serverAddress, int port, int chunkId) {
        this.buffer = buffer;
        this.serverAddress = serverAddress;
        this.port = port;
        this.chunkId = chunkId;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
            socket.send(packet);
            System.out.println("Sent chunk: " + chunkId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
