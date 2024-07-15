package client;

import client.socket.SocketRequestSender;
import shared.request.Request;
import shared.response.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles the Responses in the client side
 */

public class ServerHandler extends Thread implements ResponseHandler {

    SocketRequestSender socketRequestSender;

    ServerHandler(SocketRequestSender socketRequestSender){
        this.socketRequestSender = socketRequestSender;
    }

//    @Override
//    public void run() {
//        try {
//            while (true) {
//                Request request = socketRequestSender.getRequest();
//                if (request == null) {
//                    break;
//                }
//                Response response = request.run(this);
//                socketResponseSender.sendResponse(response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (username != null) {
//                clientMap.remove(username);
//            }
//            socketResponseSender.close();
//        }
//    }


//    @Override
//    public void run() {
//        try {
//            while (true) {
//                 response = socketResponseSender.getRequest().run(this);
//                socketResponseSender.sendResponse(response);
//            }
//        } catch (Exception e) {
//            socketResponseSender.close();
//        }
//    }


    @Override
    public void handleHiResponse(HiResponse hiResponse) {
        System.out.println("yoyoyo");
    }

    @Override
    public void handleSignInResponse(SignInResponse signInResponse) {
        if (signInResponse.isSuccessful()) System.out.println("Signed in successfully!");
        if (!signInResponse.isSuccessful()) System.out.println("This username already exists!");
    }

    @Override
    public void handleLogInResponse(LoginResponse loginResponse) {
        if (loginResponse.isSuccessful()) {
            Main.setToken(loginResponse.getToken());
            Main.setUserName(loginResponse.getUserName());
            System.out.println("Logged in successfully!");
        }
        if (!loginResponse.isSuccessful()) {
            System.out.println("The username is not found!");
        }
    }

    @Override
    public void handleGetUploadedFilesResponse(GetUploadedFilesResponse getUploadedFilesResponse) {
        if (!getUploadedFilesResponse.isValid()) System.out.println("The request is not valid. Your token may have expired!");
        else {
            System.out.println("HANDLING RESPONSE");
            ArrayList<String> filesNames = getUploadedFilesResponse.getFilesNames();
            for (String name : filesNames){
                System.out.println(name);
            }
            System.out.println("----------------------");
        }
    }

    @Override
    public void handleGetDownloadableFilesResponse(GetDownloadableFilesResponse getDownloadableFilesResponse) {
        if (!getDownloadableFilesResponse.isInvalid()) System.out.println("The request is not valid. Your token may have expired!");
        else {
            HashMap<String, Boolean> res = getDownloadableFilesResponse.getFiles();
            res.forEach((key, value) -> System.out.println(key + "     Accessible: "+ value));
        }
        System.out.println("----------------------");
    }
}
