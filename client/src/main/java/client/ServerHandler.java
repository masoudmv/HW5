package client;

import client.socket.SocketRequestSender;
import shared.FileReceiver;
import shared.FileRequest;
import shared.FileUploadManager;
//import shared.UdpFileUploadHandler;
import shared.UploadRequest;
import shared.request.Request;
import shared.response.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static client.UdpClient.PACKET_SIZE;
import static client.UdpClient.sendFileRequest;

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
    public void handleGetDownloadableFilesResponse(GetDownloadableFilesResponse getDownloadableFilesResponse) throws IOException, ClassNotFoundException {
        if (!getDownloadableFilesResponse.isInvalid()) {
            System.out.println("The request is not valid. Your token may have expired!");
        } else {
            int PACKET_SIZE = 2048;
            int SERVER_PORT = 100;
            int CLIENT_PORT = 101;

            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket(CLIENT_PORT);

            HashMap<String, Boolean> res = getDownloadableFilesResponse.getFiles();
            res.forEach((key, value) -> System.out.println(key + "     Accessible: " + value));

            String fileNameToRequest = "C:\\Users\\masoud\\Desktop\\dg5an9f-7f40bbe4-28ba-4e3f-8c14-46d948bfb0bc.png";
            FileRequest fileRequest = new FileRequest(fileNameToRequest);
            sendFileRequest(fileRequest, serverAddress, socket, SERVER_PORT);

            // Map to hold file data for each unique ID
            Map<String, FileReceiver> fileReceiverMap = new HashMap<>();

            byte[] buf = new byte[PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            while (true) {
                socket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                ObjectInputStream ois = new ObjectInputStream(bais);
                UploadRequest uploadRequest = (UploadRequest) ois.readObject();

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
            DatagramSocket socket = new DatagramSocket(101);

            List<File> filesToSend = Arrays.asList(
                    new File("C:\\Users\\masoud\\Desktop\\dg5an9f-7f40bbe4-28ba-4e3f-8c14-46d948bfb0bc.png"),
                    new File("C:\\Users\\masoud\\Desktop\\file2.txt")
            );

            // Send files to the server
            for (File file : filesToSend) {
                FileUploadManager uploadManager = new FileUploadManager(serverAddress, socket, file, tcpUploadResponse.getPort());
                uploadManager.start();
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }
}
