package shared;

import java.io.Serializable;

public class UploadRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String uniqueID;
    private String fileName;
    private int packetNumber;
    private byte[] fileData;
    private int totalParts;

    public UploadRequest(String uniqueID, String fileName, int packetNumber, byte[] fileData, int totalParts) {
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
}
