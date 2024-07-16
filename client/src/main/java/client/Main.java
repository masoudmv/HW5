package client;//package client;

import client.socket.SocketRequestSender;
import shared.request.*;
import java.io.IOException;


public class Main {
    private static String token;
    private static String userName;
    private static int numClient;



    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SocketRequestSender socketRequestSender = new SocketRequestSender();
        ServerHandler serverHandler = new ServerHandler(socketRequestSender);


        socketRequestSender.sendRequest(new SignInRequest("a", "1")).run(serverHandler);
        socketRequestSender.sendRequest(new LoginRequest("a", "1")).run(serverHandler);
        socketRequestSender.sendRequest(new GetDownloadableFilesRequest(token, "a", numClient)).run(serverHandler);
//        socketRequestSender.sendRequest(new GetDownloadableFilesRequest("a", "a")).run(serverHandler);
//        socketRequestSender.sendRequest(new TCPUploadRequest("a", token, numClient)).run(serverHandler);




//        socketRequestSender.sendRequest(new GetUploadedFilesRequest(token, "a")).run(serverHandler);

    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Main.token = token;
    }

    public static void setUserName(String userName) {
        Main.userName = userName;
    }

    public static String getUserName() {
        return userName;
    }

    public static int getNumClient() {
        return numClient;
    }

    public static void setNumClient(int numClient) {
        Main.numClient = numClient;
    }
}



