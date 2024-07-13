package server;//package server;

import server.socket.SocketStarter;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        SocketStarter socketStarter = new SocketStarter();
        socketStarter.start();
    }


}

