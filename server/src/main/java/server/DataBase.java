package server;

import shared.Model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.print.attribute.standard.RequestingUserName;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

public class DataBase {
    private static HashMap<String, User> users;

    public DataBase() {
        users = new HashMap<>();
    }
    public static List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists");
            return false; // unsuccessful
        } else {
            String hashedPassword = hashPassword(password);
            User user = new User(username, hashedPassword);
            users.put(username, user);
            System.out.println("User added successfully");
            return true; // successful
        }
    }


    public static User findUser(String userName){
        for (User user : getUsers()){
            if (user.getUsername().equals(userName)) return user;
        }
        return null;
    }


    private String hashPassword(String password) {
        try {
            int iterations = 65536;
            int keyLength = 256;
            char[] passwordChars = password.toCharArray();
            byte[] saltBytes = "somesalt".getBytes(); // In a real-world application, use a unique salt for each user.

            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keyLength);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashBytes = keyFactory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean authenticate(String username, String password) { //todo find a way to move it to authentication class
        if (users.containsKey(username)) {
            String hashedPassword = hashPassword(password);
            return users.get(username).getPassword().equals(hashedPassword);
        }
        return false;
    }


}