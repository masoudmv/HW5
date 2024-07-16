package shared;

import java.io.Serializable;
import java.io.StringReader;

public class UploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String token;
    private String uniqueID;
    private String fileName;
    private int packetNumber;
    private byte[] fileData;
    private int totalParts;

    public UploadRequest(String username, String token, String uniqueID, String fileName, int packetNumber, byte[] fileData, int totalParts) {
        this.username = username;
        this.token = token;
        this.uniqueID = uniqueID;
        this.fileName = fileName;
        this.packetNumber = packetNumber;
        this.fileData = fileData;
        this.totalParts = totalParts;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public int getTotalParts() {
        return totalParts;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
