package client;//package client;

import client.socket.SocketRequestSender;
import shared.request.*;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;


public class Main {
    private static String token;
    private static String userName;
    public static void main(String[] args) throws IOException {
        SocketRequestSender socketRequestSender = new SocketRequestSender();
        ServerHandler serverHandler = new ServerHandler(socketRequestSender);


        socketRequestSender.sendRequest(new SignInRequest("a", "1")).run(serverHandler);
        socketRequestSender.sendRequest(new LoginRequest("a", "1")).run(serverHandler);
        socketRequestSender.sendRequest(new TCPUploadRequest("a", token)).run(serverHandler);




//        socketRequestSender.sendRequest(new GetUploadedFilesRequest(token, "a")).run(serverHandler);




//        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
//        DatagramSocket socket = new DatagramSocket();
//
//        // List of files to be sent
//        List<File> filesToSend = Arrays.asList(
//                new File("C:\\Users\\masoud\\Desktop\\dg5an9f-7f40bbe4-28ba-4e3f-8c14-46d948bfb0bc.png")
////                new File("C:\\Users\\masoud\\Desktop\\file2.txt")
//        );
//
//        // Create and start a FileUploadManager for each file
//        for (File file : filesToSend) {
//            shared.FileUploadManager uploadManager = new shared.FileUploadManager(serverAddress, socket, file, 8080);
//            uploadManager.start();
//        }




//        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
//        DatagramSocket socket = new DatagramSocket();
//
//        // List of files to be sent
//        List<File> filesToSend = Arrays.asList(
////                new File("C:\\Users\\masoud\\Desktop\\file1.txt")
//                new File("C:\\Users\\masoud\\Desktop\\file2.txt")
//        );
//
//        // Create and start a FileUploadManager for each file
//        for (File file : filesToSend) {
//            FileUploadManager uploadManager = new FileUploadManager(serverAddress, socket, file, PORT);
//            uploadManager.start();
//        }






    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Main.token = token;
    }

    public static void setUserName(String userName) {
        Main.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }
}



