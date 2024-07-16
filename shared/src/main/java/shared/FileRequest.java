package shared;

import java.io.Serializable;

public class FileRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;

    public FileRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
