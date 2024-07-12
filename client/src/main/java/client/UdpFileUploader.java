package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpFileUploader {
    private static final int CHUNK_SIZE = 1024; // Size of each chunk
    private static final int PORT = 9876; // Server port

    public static void main(String[] args) throws IOException {
        File file = new File("path/to/your/file.txt");
        FileInputStream fis = new FileInputStream(file);
        InetAddress serverAddress = InetAddress.getByName("localhost"); // "127.0.0.1" ???
        long fileSize = file.length();
        int totalChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);

        for (int i = 0; i < totalChunks; i++) {
            int chunkSize = (i == totalChunks - 1) ? (int) (fileSize - (i * CHUNK_SIZE)) : CHUNK_SIZE;
            byte[] buffer = new byte[chunkSize];
            fis.read(buffer, 0, chunkSize);

            Thread thread = new Thread(new UdpSender(buffer, serverAddress, PORT, i));
            thread.start();
        }

        fis.close();
    }
}

class UdpSender implements Runnable {
    private byte[] buffer;
    private InetAddress serverAddress;
    private int port;
    private int chunkId;

    public UdpSender(byte[] buffer, InetAddress serverAddress, int port, int chunkId) {
        this.buffer = buffer;
        this.serverAddress = serverAddress;
        this.port = port;
        this.chunkId = chunkId;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
            socket.send(packet);
            System.out.println("Sent chunk: " + chunkId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
