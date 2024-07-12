package client;

import client.socket.SocketRequestSender;
import shared.response.*;

public class ServerHandler extends Thread implements ResponseHandler {
    SocketRequestSender socketRequestSender;

    ServerHandler(SocketRequestSender socketRequestSender){
        this.socketRequestSender = socketRequestSender;
    }

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
        if (loginResponse.isSuccessful()) System.out.println("Logged in successfully!");
        if (!loginResponse.isSuccessful()) System.out.println("The username is not found!");
    }
}
