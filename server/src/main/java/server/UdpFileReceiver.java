package server;//package server;
//
//import server.socket.SocketStarter;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//
//public class Main {
//    public static void main(String[] args) throws IOException {
////        SocketStarter socketStarter = new SocketStarter();
////        socketStarter.start();
//
//
//
//
//
//
//        DatagramSocket serverSocket = new DatagramSocket(8080);
//        byte[] receiveData = new byte[1024];
//
//        while (true) {
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            serverSocket.receive(receivePacket);
//            new Thread(new FileReceiver(serverSocket, receivePacket)).start();
//        }
//    }
//}






import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UdpFileReceiver {
    private static final int PORT = 8080; // Server port
    private static final int CHUNK_SIZE = 10; // Size of each chunk
    private static final Map<Integer, byte[]> fileChunks = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(PORT);
        byte[] buffer = new byte[CHUNK_SIZE];
        boolean receiving = true;

        while (receiving) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            int chunkId = extractChunkId(packet.getData());
            fileChunks.put(chunkId, packet.getData());
            System.out.println("Received chunk: " + chunkId);

            // Assuming you have a way to determine when the file is fully received
            if (allChunksReceived()) {
                receiving = false;
            }
        }

        socket.close();
        reconstructFile();
    }

    private static int extractChunkId(byte[] data) {
        // Extract chunk ID from data. This is just an example.
        return data[0]; // Implement according to your protocol
    }

    private static boolean allChunksReceived() {
        // Implement logic to check if all chunks are received
        return false;
    }

    private static void reconstructFile() throws IOException {
        try (FileOutputStream fos = new FileOutputStream("C:\\Users\\masoud\\Desktop\\hello.txt")) {
            for (int i = 0; i < fileChunks.size(); i++) {
                fos.write(fileChunks.get(i));
            }
        }
    }
}
