package client;

import client.socket.SocketRequestSender;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;
import shared.response.SignInResponse;

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

    }
}
