package shared;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileReceiver {
    private String fileName;
    private Map<Integer, byte[]> receivedPackets = new HashMap<>();
    private int packetsReceived = 0;

    public FileReceiver(String fileName) {
        this.fileName = "C:\\Users\\masoud\\Desktop\\source\\" + fileName;
    }

    public synchronized void addPacket(int packetNumber, byte[] data) {
        receivedPackets.put(packetNumber, data);
        packetsReceived++;
    }

    public void finish() throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        for (int i = 0; i < receivedPackets.size(); i++) {
            byte[] data = receivedPackets.get(i);
            if (data != null) {
                fos.write(data);
            }
        }
        fos.close();
    }

    public String getFileName() {
        return fileName;
    }
}