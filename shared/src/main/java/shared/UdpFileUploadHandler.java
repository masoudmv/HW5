//package shared;
//
//import java.io.*;
//import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
//import java.util.UUID;
//
//public class UdpFileUploadHandler extends Thread {
//    private InetAddress serverAddress;
//    private DatagramSocket socket;
//    private File file;
//    private int port;
//
//    public UdpFileUploadHandler(InetAddress serverAddress, DatagramSocket socket, File file, int port) {
//        this.serverAddress = serverAddress;
//        this.socket = socket;
//        this.file = file;
//        this.port = port;
//    }
//
//    @Override
//    public void run() {
//        try {
////            String fileName = file.getName();
////            String uniqueID = UUID.randomUUID().toString();
////            FileInputStream fis = new FileInputStream(file);
////
////            byte[] buffer = new byte[2048];
////            int bytesRead;
////            int partNumber = 0;
////            int totalParts = (int) Math.ceil((double) file.length() / 2048);
////
////            while ((bytesRead = fis.read(buffer)) != -1) {
////                byte[] dataToSend = new byte[bytesRead];
////                System.arraycopy(buffer, 0, dataToSend, 0, bytesRead);
////
//////                UploadRequest request = new UploadRequest( ,uniqueID, fileName, partNumber, dataToSend, totalParts);
////                ByteArrayOutputStream baos = new ByteArrayOutputStream();
////                ObjectOutputStream oos = new ObjectOutputStream(baos);
////                oos.writeObject(request);
////                oos.flush();
////                byte[] dataPacket = baos.toByteArray();
////
////                DatagramPacket packet = new DatagramPacket(dataPacket, dataPacket.length, serverAddress, port);
////                socket.send(packet);
////
////                System.out.println("Sent part " + partNumber + " of file " + fileName);
////                partNumber++;
//            }
//
//            fis.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
