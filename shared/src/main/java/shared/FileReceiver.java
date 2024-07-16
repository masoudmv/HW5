package shared;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReceiver {
    private String fileName;
    private int totalParts;
    private Map<Integer, byte[]> receivedPackets = new HashMap<>();

    public FileReceiver(String fileName, int totalParts) {
        this.fileName = "C:\\Users\\masoud\\Desktop\\source\\" + fileName;
        this.totalParts = totalParts;
    }

    public synchronized void addPacket(int packetNumber, byte[] data) {
        receivedPackets.put(packetNumber, data);
    }

    public boolean isComplete() {
        return receivedPackets.size() == totalParts;
    }

    public void finish() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            for (int i = 0; i < totalParts; i++) {
                fos.write(receivedPackets.get(i));
            }
        }
    }

    public String getFileName() {
        return fileName;
    }
}