package shared.request;

import java.io.Serializable;

public class UploadRequest implements Serializable {
    private static final long serialVersionUID = 1L; // ???
    private String uniqueID;
    private String fileName;
    private int packetNumber;
    private byte[] fileData;
    private boolean isLastPacket;

    public UploadRequest(String uniqueID, String fileName, int packetNumber, byte[] fileData, boolean isLastPacket) {
        this.uniqueID = uniqueID;
        this.fileName = fileName;
        this.packetNumber = packetNumber;
        this.fileData = fileData;
        this.isLastPacket = isLastPacket;
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

    public boolean isLastPacket() {
        return isLastPacket;
    }
}
