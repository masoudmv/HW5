package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;

@JsonTypeName("UploadRequest")
public class UploadRequest {

    public UploadRequest() throws FileNotFoundException { // todo

//        File file = new File("path/to/your/file.txt");
//        FileInputStream fis = new FileInputStream(file);
//        InetAddress serverAddress = InetAddress.getByName("localhost");
//        long fileSize = file.length();
//        int totalChunks = (int) Math.ceil((double) fileSize / CHUNK_SIZE);
//
//        for (int i = 0; i < totalChunks; i++) {
//            int chunkSize = (i == totalChunks - 1) ? (int) (fileSize - (i * CHUNK_SIZE)) : CHUNK_SIZE;
//            byte[] buffer = new byte[chunkSize];
//            fis.read(buffer, 0, chunkSize);
//
//            Thread thread = new Thread(new UdpSender(buffer, serverAddress, PORT, i));
//            thread.start();
//        }
//
//        fis.close();

    }
}
