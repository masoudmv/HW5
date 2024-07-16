package shared;

import java.io.Serializable;

public class FileRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String username;
    private String token;

    public FileRequest(String fileName, String username, String token) {
        this.fileName = fileName;
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getFileName() {
        return fileName;
    }
}
