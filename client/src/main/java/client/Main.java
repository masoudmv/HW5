package client;


import client.socket.SocketRequestSender;
import shared.request.HiRequest;
import shared.request.LoginRequest;
import shared.request.SignInRequest;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;
import shared.response.SignInResponse;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
//        SocketRequestSender socketRequestSender = new SocketRequestSender();
//        ServerHandler serverHandler = new ServerHandler(socketRequestSender);
//        socketRequestSender.sendRequest(new SignInRequest("a", "1")).run(serverHandler);
//        socketRequestSender.sendRequest(new LoginRequest("a", "1")).run(serverHandler);
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();





        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");

        // Example: Uploading multiple files
        List<String> filesToUpload = Arrays.asList("C:\\Users\\masoud\\Desktop\\NetworkClass\\client\\src\\file1.txt",
                "C:\\Users\\masoud\\Desktop\\NetworkClass\\client\\src\\file2.txt");
        for (String filePath : filesToUpload) {
            new Thread(new FileSender(clientSocket, serverAddress, filePath)).start();
        }

    }



}