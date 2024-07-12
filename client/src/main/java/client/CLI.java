package client;

import java.io.File;
import java.util.Scanner;

public class CLI {
    private static Scanner scanner = new Scanner(System.in);

    public static void showInitialOptions() {
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


    public static void showAuthenticatedOptions() {
        System.out.println("Choose an option:");
        System.out.println("1. Choose File for Upload");
        System.out.println("2. Choose File for Download");
        System.out.println("3. Access Request");
        System.out.println("4. Log Out");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                chooseFileForUpload();
                break;
            case 2:
                chooseFileForDownload();
                break;
            case 3:
                accessRequest();
                break;
            case 4:
                logOut();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }


    private static void signIn() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();





        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // check if the username already exists

//        db.addUser(username, password);
    }

    private static void logIn() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

//        if (db.authenticate(username, password)) {
//            currentUser = db.getUser(username);
//            System.out.println("Logged in successfully.");
//        } else {
//            System.out.println("Invalid username or password.");
//        }
    }

    private static void logOut() {
//        currentUser = null;
        System.out.println("Logged out successfully.");
        showInitialOptions();
    }

    private static void chooseFileForUpload() {
        System.out.print("Enter the path of the file to upload: ");
        String filePath = scanner.nextLine();
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }

//        List<File> files = currentUser.getFiles();
//        files.add(file);
//        currentUser.setFiles(files);
//        System.out.println("File uploaded successfully.");
    }

    private static void chooseFileForDownload() {
//        List<File> files = currentUser.getFiles();
//        if (files == null || files.isEmpty()) {
//            System.out.println("No files available for download.");
//            return;
//        }
//
//        System.out.println("Files available for download:");
//        for (int i = 0; i < files.size(); i++) {
//            System.out.println((i + 1) + ". " + files.get(i).getName());
//        }
//
//        System.out.print("Enter the number of the file to download: ");
//        int fileNumber = scanner.nextInt();
//        scanner.nextLine();  // Consume newline
//
//        if (fileNumber < 1 || fileNumber > files.size()) {
//            System.out.println("Invalid file number.");
//            return;
//        }

//        File fileToDownload = files.get(fileNumber - 1);
//        System.out.println("Downloading file: " + fileToDownload.getName());
        // Add file download logic here
    }

    private static void accessRequest() {
//        System.out.print("Enter the file name for access request: ");
//        String fileName = scanner.nextLine();
//
//        List<File> files = currentUser.getFiles();
//        for (File file : files) {
//            if (file.getName().equals(fileName)) {
//                System.out.println("Access granted to file: " + fileName);
//                return;
//            }
//        }
//        System.out.println("Access denied. File not found.");
    }
}

