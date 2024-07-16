package client;

import client.socket.SocketRequestSender;
import shared.FileReceiver;
import shared.FileRequest;
import shared.FileUploadManager;
//import shared.UdpFileUploadHandler;
import shared.UploadRequest;
import shared.request.Request;
import shared.response.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//import static client.UdpClient.PACKET_SIZE;
//import static client.UdpClient.sendFileRequest;

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
    public void handleLogInResponse(LoginResponse loginResponse) throws IOException, ClassNotFoundException {
        if (loginResponse.isSuccessful()) {

            Main.setToken(loginResponse.getToken());
            Main.setUserName(loginResponse.getUserName());
            Main.setNumClient(loginResponse.getNumClient());

            System.out.println("Logged in successfully!");
            CLI.showAuthenticatedOptions();

            try {
                String folderPath = "./client/DataBase/client" + Main.getNumClient(); //TODO
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
            CLI.showInitialOptions();
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
            DatagramSocket socket = null;

            try {
                socket = new DatagramSocket(CLIENT_PORT);

                HashMap<String, Boolean> res = getDownloadableFilesResponse.getFiles();
                res.forEach((key, value) -> System.out.println(key + "     Accessible: " + value));

                // Reading user input safely
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
                        fileNameToRequest, getDownloadableFilesResponse.getUsername(), getDownloadableFilesResponse.getToken());
                sendFileRequest(fileRequest, serverAddress, socket, SERVER_PORT);

                String path = "./client/DataBase/client" + Main.getNumClient() + "/";

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
                    FileReceiver fileReceiver = fileReceiverMap.computeIfAbsent(uniqueID, k -> new FileReceiver(uploadRequest.getFileName(), uploadRequest.getTotalParts(), path));

                    fileReceiver.addPacket(uploadRequest.getPacketNumber(), uploadRequest.getFileData());
                    System.out.println("Received packet number: " + uploadRequest.getPacketNumber() + " for file ID: " + uniqueID);

                    if (fileReceiver.isComplete()) {
                        fileReceiver.finish();
                        fileReceiverMap.remove(uniqueID);
                        System.out.println("File received and saved as " + fileReceiver.getFileName());

                        // Close the socket after receiving the file
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Socket closed.");
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

            int numFiles = tcpUploadResponse.getPaths().size();
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket(101);

            List<File> filesToSend = new ArrayList<>();
//                    /
//                    new Lis[numFiles];
//                    Arrays.asList( // TODO
//                    new File(),
////                    new File(tcpUploadResponse.getPath())
//            );


            for (int i = 0; i < numFiles; i++) {
                filesToSend.add(new File(tcpUploadResponse.getPaths().get(i)));
            }

            // Send files to the server
            for (File file : filesToSend) {
                FileUplocdadManager uploadManager = new FileUploadManager(
                        Main.getUserName(), Main.getToken(), serverAddress, socket, file, tcpUploadResponse.getPort());
                uploadManager.start();
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
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
