package shared.response;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

@JsonTypeName("TCPUploadResponse")
public class TCPUploadResponse implements Response{
    private ArrayList<String> paths;
    private boolean valid;
    private int port;

    public TCPUploadResponse(ArrayList<String> paths, boolean valid, int port) {
        this.paths = paths;
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

    public ArrayList<String> getPaths() {
        return paths;
    }

    @Override
    public void run(ResponseHandler responseHandler) throws SocketException, UnknownHostException {
        responseHandler.handleTCPUploadResponse(this);
    }
}
