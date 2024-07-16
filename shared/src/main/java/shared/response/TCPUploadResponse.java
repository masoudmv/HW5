package shared.response;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.net.SocketException;
import java.net.UnknownHostException;

@JsonTypeName("TCPUploadResponse")
public class TCPUploadResponse implements Response{
    String path;
    private boolean valid;
    private int port;

    public TCPUploadResponse(String path, boolean valid, int port) {
        this.path = path;
        this.valid = valid;
        this.port = port;
    }

    public TCPUploadResponse(boolean valid) {
        this.valid = valid;
    }

    public TCPUploadResponse() {
    }

    public int getPort() {
        return port;
    }

    public boolean isValid() {
        return valid;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void run(ResponseHandler responseHandler) throws SocketException, UnknownHostException {
        responseHandler.handleTCPUploadResponse(this);
    }
}
