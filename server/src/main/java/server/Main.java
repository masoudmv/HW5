package server;

import server.socket.SocketStarter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {
    public static void main(String[] args) throws IOException {
//        SocketStarter socketStarter = new SocketStarter();
//        socketStarter.start();






        DatagramSocket serverSocket = new DatagramSocket(8080);
        byte[] receiveData = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            new Thread(new FileReceiver(serverSocket, receivePacket)).start();
        }
    }
}