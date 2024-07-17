package client;

import client.socket.SocketRequestSender;
import shared.request.*;
import shared.response.ResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CLI {
    private static Scanner scanner = new Scanner(System.in);
    private static SocketRequestSender socketRequestSender;
    private static ServerHandler serverHandler;

    public CLI(SocketRequestSender socketRequestSender, ServerHandler serverHandler) {
        CLI.socketRequestSender = socketRequestSender;
        CLI.serverHandler = serverHandler;
    }

    public static void showInitialOptions() throws IOException, ClassNotFoundException {
        System.out.println("*****************");
        System.out.println("Choose an option:");
        System.out.println("1. Sign In");
        System.out.println("2. Log In");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                signIn();
                break;
            case 2:
                logIn();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    public static void showAuthenticatedOptions() throws IOException, ClassNotFoundException {
        System.out.println("*****************");
        System.out.println("Choose an option:");
        System.out.println("1. Choose File for Upload");
        System.out.println("2. See Uploaded Files");
        System.out.println("3. Choose File for Download");
        System.out.println("4. Access Request");
        System.out.println("5. Log Out");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                chooseFileForUpload();
                break;
            case 2:
                socketRequestSender.sendRequest(
                        new GetUploadedFilesRequest(Main.getToken(), Main.getUserName())).run(serverHandler);
                showAuthenticatedOptions();
                break;
            case 3:
                chooseFileForDownload();
                showAuthenticatedOptions();
                break;
            case 4:
                accessRequest();
                break;
            case 5:
                logOut();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    private static void signIn() throws IOException, ClassNotFoundException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        socketRequestSender.sendRequest(new SignInRequest(username, password)).run(serverHandler);
        showInitialOptions();
    }

    private static void logIn() throws IOException, ClassNotFoundException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        socketRequestSender.sendRequest(new LoginRequest(username, password)).run(serverHandler);
//        showAuthenticatedOptions();

    }

    private static void logOut() throws IOException, ClassNotFoundException {
        Main.setUserName(null);
        Main.setToken(null);
        Main.setNumClient(0);
        System.out.println("Logged out successfully.");
        showInitialOptions();
    }



    private static void chooseFileForUpload() throws IOException, ClassNotFoundException {
        ArrayList<String> paths = new ArrayList<>();


        System.out.print("Enter the path of the file to upload: ");
        String filePath = scanner.nextLine();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

        paths.add(filePath);

        System.out.println("Enter another File path or 'end' to continue.");
        String path = scanner.nextLine();
        while (!path.equals("end")){
            paths.add(path);
            System.out.println("Enter another File path or 'end' to continue.");
            path = scanner.nextLine();
        }

        socketRequestSender.sendRequest(
                new TCPUploadRequest(paths, Main.getUserName(), Main.getToken(), Main.getNumClient())).run(serverHandler);
        showAuthenticatedOptions();
    }

    private static void chooseFileForDownload() throws IOException, ClassNotFoundException {

        socketRequestSender.sendRequest(
                new GetDownloadableFilesRequest(Main.getToken(), Main.getUserName(), Main.getNumClient())).run(serverHandler);


    }

    private static void accessRequest() {

    }
}

