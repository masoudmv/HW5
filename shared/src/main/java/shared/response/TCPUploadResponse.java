package shared.response;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.net.SocketException;
import java.net.UnknownHostException;

@JsonTypeName("TCPUploadResponse")
public class TCPUploadResponse implements Response{
    private boolean valid;
    private int port;

    public TCPUploadResponse(boolean valid, int port) {
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

    @Override
    public void run(ResponseHandler responseHandler) throws SocketException, UnknownHostException {
        responseHandler.handleTCPUploadResponse(this);
    }
}
