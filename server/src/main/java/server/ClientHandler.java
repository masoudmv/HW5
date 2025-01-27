package server;

import server.socket.SocketResponseSender;
import shared.*;
import shared.Model.User;
import shared.request.*;
import shared.response.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static server.DataBase.findUser;
//import static server.UdpServer.*;

public class ClientHandler extends Thread implements RequestHandler {
    private static final ConcurrentHashMap<String, SocketResponseSender> clientMap = new ConcurrentHashMap<>();
    private final SocketResponseSender socketResponseSender;
    private final DataBase dataBase;
    private UdpFileUploadHandler udpFileUploadHandler;
    private String username;
    int port = 100;
    DatagramSocket socket = new DatagramSocket(port); // Open datagram socket on port 100

    public ClientHandler(SocketResponseSender socketResponseSender, DataBase dataBase) throws SocketException {
        this.dataBase = dataBase;
        this.socketResponseSender = socketResponseSender;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Response response = socketResponseSender.getRequest().run(this);
                socketResponseSender.sendResponse(response);
            }
        } catch (Exception e) {
            if (udpFileUploadHandler != null) {
                udpFileUploadHandler.stopHandler();
            }
            socketResponseSender.close();
        }
    }

    @Override
    public Response handleSignInRequest(SignInRequest signInRequest) {
        String username = signInRequest.getUsername();
        String password = signInRequest.getPassword();

        boolean successful = dataBase.addUser(username, password);
        return new SignInResponse(successful);
    }

    @Override
    public Response handleLoginRequest(LoginRequest loginRequest) throws IOException {
        this.username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean successful = dataBase.authenticate(username, password);
        if (successful) {
            clientMap.put(username, socketResponseSender);
            String folderPath = "./server/DataBase/client" + clientMap.size();
            Path path = Paths.get(folderPath);
            Files.createDirectories(path);
        }
        String token = JwtUtil.generateToken(username);
        return new LoginResponse(successful, token, username, clientMap.size());
    }

    @Override
    public Response handleGetUploadedFilesRequest(GetUploadedFilesRequest getUploadedFilesRequest) {
        String token = getUploadedFilesRequest.getToken();
        String username = getUploadedFilesRequest.getUserName();

        boolean isValid = JwtUtil.validateToken(token, username);
        if (!isValid) return new GetUploadedFilesResponse(false);

        User user = findUser(username);
        assert user != null;
        System.out.println("Sending uploaded Names response ...");
        ArrayList<String> out = user.getFileStrings();
        return new GetUploadedFilesResponse(true, out);
    }

    @Override
    public Response handleTCPUploadRequest(TCPUploadRequest tcpUploadRequest) throws IOException, ClassNotFoundException {
        String token = tcpUploadRequest.getToken();
        String username = tcpUploadRequest.getUsername();

        boolean isValid = JwtUtil.validateToken(token, username);
        if (!isValid) return new TCPUploadResponse(false);

//        int port = 100;
//        DatagramSocket socket = new DatagramSocket(port); // Open datagram socket on port 100
        byte[] buf = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        int numClient = tcpUploadRequest.getNumClient();

        udpFileUploadHandler = new UdpFileUploadHandler(socket, packet, port, numClient);
        udpFileUploadHandler.start();

        return new TCPUploadResponse(tcpUploadRequest.getPaths(), true, port); // Placeholder response, adjust as needed
    }

    @Override
    public Response handleGetDownloadableFilesRequest(GetDownloadableFilesRequest getDownloadableFilesRequest) throws SocketException {
        String token = getDownloadableFilesRequest.getToken();
        String username = getDownloadableFilesRequest.getUsername();

        boolean isValid = JwtUtil.validateToken(token, username);
        if (!isValid) return new GetDownloadableFilesResponse(false);


        User user = findUser(username);
//        assert user != null;
        if (user == null) return new GetDownloadableFilesResponse(false);

//        if (user == null) return new GetDownloadableFilesResponse(false);
//        System.out.println("Sending uploaded Names response ...");
        ArrayList<String> out = user.getFileStrings();

//        System.out.println("SENDING response ...");


        byte[] buf = new byte[2048];
//        System.out.println("i am hereAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        int numClient = getDownloadableFilesRequest.getNumClient();

        udpFileUploadHandler = new UdpFileUploadHandler(socket, packet, port, numClient);
        udpFileUploadHandler.start();


        return new GetDownloadableFilesResponse(true, out);
    }

    @Override
    public Response handleAccessRequest(AccessRequest accessRequest) {
        return null;
    }
}
