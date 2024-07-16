package client;

import client.socket.SocketRequestSender;
import shared.UdpFileUploadHandler;
import shared.request.Request;
import shared.response.*;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * This class handles the Responses in the client side
 */
public class ServerHandler extends Thread implements ResponseHandler {

    private final SocketRequestSender socketRequestSender;

    public ServerHandler(SocketRequestSender socketRequestSender) {
        this.socketRequestSender = socketRequestSender;
    }

//    @Override
//    public void run() {
//        try {
//            while (true) {
//                Response response = socketRequestSender.getResponse();
//                if (response != null) {
//                    response.run(this);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            socketRequestSender.close();
//        }
//    }

    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("yoyoyo");
    }

    @Override
    public void handleSignInResponse(SignInResponse signInResponse) {
        if (signInResponse.isSuccessful()) System.out.println("Signed in successfully!");
        else System.out.println("This username already exists!");
    }

    @Override
    public void handleLogInResponse(LoginResponse loginResponse) {
        if (loginResponse.isSuccessful()) {
            Main.setToken(loginResponse.getToken());
            Main.setUserName(loginResponse.getUserName());
            System.out.println("Logged in successfully!");

            try {
                String folderPath = "./client/src/client" + loginResponse.getNumClient();
                Path path = Paths.get(folderPath);
                Files.createDirectories(path);
                System.out.println("Folder and necessary parent directories created successfully!");
            } catch (IOException e) {
                System.err.println("Failed to create folder: " + e.getMessage());
            }
        } else {
            System.out.println("The username is not found!");
        }
    }

    @Override
    public void handleGetUploadedFilesResponse(GetUploadedFilesResponse getUploadedFilesResponse) {
        if (!getUploadedFilesResponse.isValid()) {
            System.out.println("The request is not valid. Your token may have expired!");
        } else {
            System.out.println("HANDLING RESPONSE");
            ArrayList<String> filesNames = getUploadedFilesResponse.getFilesNames();
            for (String name : filesNames) {
                System.out.println(name);
            }
            System.out.println("----------------------");
        }
    }

    @Override
    public void handleGetDownloadableFilesResponse(GetDownloadableFilesResponse getDownloadableFilesResponse) {
        if (!getDownloadableFilesResponse.isInvalid()) {
            System.out.println("The request is not valid. Your token may have expired!");
        } else {
            HashMap<String, Boolean> res = getDownloadableFilesResponse.getFiles();
            res.forEach((key, value) -> System.out.println(key + "     Accessible: " + value));
        }
        System.out.println("----------------------");
    }

    @Override
    public void handleTCPUploadResponse(TCPUploadResponse tcpUploadResponse) {
        if (!tcpUploadResponse.isValid()) {
            System.out.println("The request is not valid. Your token may have expired!");
            return;
        }

        System.out.println("Starting UDP file upload...");

        try {
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();

            List<File> filesToSend = Arrays.asList(
                    new File("C:\\Users\\masoud\\Desktop\\dg5an9f-7f40bbe4-28ba-4e3f-8c14-46d948bfb0bc.png")
            );

            // Send files to the server
            for (File file : filesToSend) {
                UdpFileUploadHandler uploadManager = new UdpFileUploadHandler(serverAddress, socket, file, tcpUploadResponse.getPort());
                uploadManager.start();
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }
}
