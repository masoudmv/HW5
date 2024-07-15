package client.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import shared.request.Request;
import shared.response.HiResponse;
import shared.response.Response;
import shared.response.ResponseHandler;
import shared.response.SignInResponse;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class SocketRequestSender {
    private final Socket socket;
    private final PrintStream printStream;
    private final Scanner scanner;
    private final ObjectMapper objectMapper;


//    private final DatagramSocket socket;
//    private final InetAddress serverAddress;
//    private final int serverPort;

    public SocketRequestSender() throws IOException {
        this.socket = new Socket("127.0.0.1", 8080);
        printStream = new PrintStream(socket.getOutputStream()); // used to send Request to server
        scanner = new Scanner(socket.getInputStream()); // used to receive Response/Request? from server
        objectMapper = new ObjectMapper();
    }


    public Response sendRequest(Request request) throws IOException {
        try {
            printStream.println(objectMapper.writeValueAsString(request));
            return objectMapper.readValue(scanner.nextLine(), Response.class);
        } catch (Exception e) {
            System.out.println(e);
            close();
            throw e;
        }
    }

    public Request getRequest() {
        try {
            if (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                return objectMapper.readValue(s, Request.class);
            } else {
                System.out.println("No line found");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to send a response
    public void sendResponse(Response response) {
        try {
            printStream.println(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            scanner.close();
            printStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
