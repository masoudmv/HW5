package server.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import shared.request.Request;
import shared.response.Response;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketResponseSender {
    public final Scanner scanner;
    private final Socket socket;
    private final PrintStream printStream;
    private final ObjectMapper objectMapper;

    public SocketResponseSender(Socket socket) throws IOException {
        this.socket = socket;
        printStream = new PrintStream(socket.getOutputStream()); // using outputStream server can send Request to client
        scanner = new Scanner(socket.getInputStream()); // using InputStream server can receive Response from client
        objectMapper = new ObjectMapper();
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

    public void sendResponse(Response response) {
        try {
            printStream.println(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            System.out.println("asd");
        }
    }

    public void sendRequest(Request request) {
        try {
            printStream.println(objectMapper.writeValueAsString(request));
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
