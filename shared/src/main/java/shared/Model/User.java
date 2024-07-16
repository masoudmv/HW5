package shared.Model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User extends Model {
    private String username;
    private String password;
    private final List<File> files = new ArrayList<>();
    private List<String> hasAccessTo;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        hasAccessTo = new ArrayList<>();
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getHasAccessTo() {
        return hasAccessTo;
    }

    public List<File> getFiles() {
        return files;
    }

    public ArrayList<String> getFileStrings(){
        ArrayList<String> fileNames = new ArrayList<>();
        for (File file : files){
            fileNames.add(file.getName());
        }
        return fileNames;
    }


    public void addFile(File file){
        this.files.add(file);
        this.hasAccessTo.add(file.getName());
    }

}
