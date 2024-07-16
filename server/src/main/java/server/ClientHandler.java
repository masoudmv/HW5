package server;

import server.socket.SocketResponseSender;
import shared.FileReceiver;
import shared.JwtUtil;
import shared.Model.User;
import shared.request.*;
import shared.response.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static server.DataBase.findUser;

public class ClientHandler extends Thread implements RequestHandler {
    private static final ConcurrentHashMap<String, SocketResponseSender> clientMap = new ConcurrentHashMap<>();
    private final SocketResponseSender socketResponseSender;
    private final DataBase dataBase;
    private String username;

    public ClientHandler(SocketResponseSender socketResponseSender, DataBase dataBase) {
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
    public Response handleLoginRequest(LoginRequest loginRequest) {
        this.username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean successful = dataBase.authenticate(username, password);
        if (successful) {
            clientMap.put(username, socketResponseSender);
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


//        System.out.println("UUUUUUUUDDDDDDDDDDPPPPPPPPPPP");
        // Start the UDP file upload handler in a new thread
        int port = 100;
        UdpFileUploadHandler udpFileUploadHandler = new UdpFileUploadHandler(port);
        udpFileUploadHandler.start();

        return new TCPUploadResponse(true, port); // Placeholder response, adjust as needed
    }

    @Override
    public Response handleAccessRequest(AccessRequest accessRequest) {
        return null;
    }

    @Override
    public Response handleGetDownloadableFilesRequest(GetDownloadableFilesRequest getDownloadableFilesRequest) {
        String token = getDownloadableFilesRequest.getToken();
        String username = getDownloadableFilesRequest.getUserName();
        System.out.println("Sending response ...");

        boolean isValid = JwtUtil.validateToken(token, username);
        if (!isValid) return new GetUploadedFilesResponse(false);

        HashMap<String, Boolean> res = new HashMap<>();
        List<String> accessables = findUser(username).getHasAccessTo();
        List<User> users = DataBase.getUsers();
        for (User user : users) {
            ArrayList<String> files = user.getFileStrings();
            for (String name : files) {
                if (accessables.contains(name)) res.put(name, true);
                else res.put(name, false);
            }
        }

        return new GetDownloadableFilesResponse(true, res);
    }
}
