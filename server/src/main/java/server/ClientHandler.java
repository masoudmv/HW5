package server;

import server.socket.SocketResponseSender;
import shared.JwtUtil;
import shared.Model.User;
import shared.request.*;
import shared.response.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static server.DataBase.findUser;

public class ClientHandler extends Thread implements RequestHandler {
    private static final ConcurrentHashMap<String, SocketResponseSender> clientMap = new ConcurrentHashMap<>();
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;
    private String username;  // This field stores the username of the logged-in client


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


//    @Override
//    public Response handleHiRequest(HiRequest hiRequest) {
//        System.out.println("naisbdaVGFYDVS");
//        return new HiResponse();
//    }

    @Override
    public Response handleSignInRequest(SignInRequest signInRequest) { //todo
        String username = signInRequest.getUsername();
        String password = signInRequest.getPassword();

        boolean successful = dataBase.addUser(username, password);
        return new SignInResponse(successful);
    }

    @Override
    public Response handleAccessRequest(AccessRequest accessRequest) {
//        String targetUsername = accessRequest.getTargetUsername();
//        SocketResponseSender targetClient = clientMap.get(targetUsername);
//
//        if (targetClient != null) {
//            // Send AcceptRequest to the target client
//            targetClient.sendRequest(new AcceptRequest(accessRequest.getRequestingUsername()));
//            return new AccessResponse("Access request sent to target client", targetUsername, accessRequest.getRequestingUsername());
//        } else {
//            return new AccessResponse("Target client not found", targetUsername, accessRequest.getRequestingUsername());
//        }
        return null;
    }

//    @Override
//    public Response handleAcceptRequest(AcceptRequest acceptRequest) {
//        String requestingUsername = acceptRequest.getRequestingUsername();
//        SocketResponseSender requestingClient = clientMap.get(requestingUsername);
//
//        if (requestingClient != null) {
//            // Send an AccessResponse to the original requester
//            requestingClient.sendRequest(new AccessResponse("Access accepted", null, requestingUsername));
//        }
//        return null;
//    }



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
    public Response handleGetDownloadableFilesRequest(GetDownloadableFilesRequest getDownloadableFilesRequest) {
        String token = getDownloadableFilesRequest.getToken();
        String username = getDownloadableFilesRequest.getUserName();
        System.out.println("Sending response ...");

        boolean isValid = JwtUtil.validateToken(token, username);
        if (!isValid) return new GetUploadedFilesResponse(false);


        HashMap<String, Boolean> res = new HashMap<>();
        List<String> accessables =  findUser(username).getHasAccessTo();
        List<User> users = DataBase.getUsers();
        for (User user : users){
            ArrayList<String> files = user.getFileStrings();
            for (String name : files){
                if (accessables.contains(name)) res.put(name, true);
                else res.put(name, false);
            }
        }

        return new GetDownloadableFilesResponse(true, res);
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
        return new LoginResponse(successful, token, username); //
    }
}
