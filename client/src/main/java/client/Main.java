package client;//package client;


import client.socket.SocketRequestSender;
import shared.request.LoginRequest;
import shared.request.SignInRequest;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        SocketRequestSender socketRequestSender = new SocketRequestSender();
        ServerHandler serverHandler = new ServerHandler(socketRequestSender);
        socketRequestSender.sendRequest(new SignInRequest("a", "1")).run(serverHandler);
        socketRequestSender.sendRequest(new LoginRequest("a", "1")).run(serverHandler);

    }
    
}



