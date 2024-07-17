package client;

import client.socket.SocketRequestSender;
import shared.FileReceiver;
import shared.FileRequest;
import shared.FileUploadManager;
import shared.UploadRequest;
import shared.request.Request;
import shared.response.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ServerHandler extends Thread implements ResponseHandler {

    private final SocketRequestSender socketRequestSender;
    InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
    int CLIENT_PORT = 101;
    int PACKET_SIZE = 2048;
    int SERVER_PORT = 100;

    DatagramSocket socket = new DatagramSocket(CLIENT_PORT);

    public ServerHandler(SocketRequestSender socketRequestSender) throws SocketException, UnknownHostException {
        this.socketRequestSender = socketRequestSender;
    }

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
    public void handleLogInResponse(LoginResponse loginResponse) throws IOException, ClassNotFoundException {
        if (loginResponse.isSuccessful()) {
            Main.setToken(loginResponse.getToken());
            Main.setUserName(loginResponse.getUserName());
            Main.setNumClient(loginResponse.getNumClient());

            System.out.println("Logged in successfully!");
            CLI.showAuthenticatedOptions();

            try {
                String folderPath = "./client/DataBase/client" + Main.getNumClient();
                Path path = Paths.get(folderPath);
                Files.createDirectories(path);
                System.out.println("Folder and necessary parent directories created successfully!");
            } catch (IOException e) {
                System.err.println("Failed to create folder: " + e.getMessage());
            }
        } else {
            System.out.println("The username is not found!");
            CLI.showInitialOptions();
        }
    }

    @Override
    public void handleGetUploadedFilesResponse(GetUploadedFilesResponse getUploadedFilesResponse) throws IOException, ClassNotFoundException {
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
        if (!getDownloadableFilesResponse.isValid()) {
            System.out.println("The request is not valid. Your token may have expired!");
        } else {
            System.out.println("PRINTING FILES");

            ArrayList<String> files = getDownloadableFilesResponse.getFilesList();
            for (String name : files) {
                System.out.println(name);
            }

            Scanner scanner = new Scanner(System.in);
            String fileNameToRequest = null;

            System.out.println("Enter the file name to request:");
            if (scanner.hasNextLine()) {
                fileNameToRequest = scanner.nextLine();
            } else {
                System.out.println("No input received.");
                return;
            }

            FileRequest fileRequest = new FileRequest(
                    fileNameToRequest, Main.getUserName(), Main.getToken());
            sendFileRequest(fileRequest, serverAddress, socket, SERVER_PORT);

            String path = "./client/DataBase/client" + Main.getNumClient() + "/";

            FileReceiverHandler fileReceiverHandler = new FileReceiverHandler(socket, serverAddress, PACKET_SIZE, path);
            fileReceiverHandler.start();
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

        int numFiles = tcpUploadResponse.getPaths().size();
        List<File> filesToSend = new ArrayList<>();

        for (int i = 0; i < numFiles; i++) {
            filesToSend.add(new File(tcpUploadResponse.getPaths().get(i)));
        }

        for (File file : filesToSend) {
            FileUploadManager uploadManager = new FileUploadManager(
                    Main.getUserName(), Main.getToken(), serverAddress, socket, file, tcpUploadResponse.getPort());
            uploadManager.start();
        }
    }

    private static void sendFileRequest(FileRequest fileRequest, InetAddress serverAddress, DatagramSocket socket, int port) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(fileRequest);
        oos.flush();
        byte[] dataToSend = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, serverAddress, port);
        socket.send(packet);
        System.out.println("Sent file request for: " + fileRequest.getFileName());
    }
}
