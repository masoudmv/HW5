package client;


import client.socket.SocketRequestSender;
import shared.request.HiRequest;
import shared.request.SignInRequest;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;
import shared.response.SignInResponse;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        SocketRequestSender socketRequestSender = new SocketRequestSender();
        ServerHandler serverHandler = new ServerHandler(socketRequestSender);
        socketRequestSender.sendRequest(new SignInRequest("a", "1")).run(serverHandler);

//        serverHandler.handleSignInResponse((SignInResponse) response);
    }



}