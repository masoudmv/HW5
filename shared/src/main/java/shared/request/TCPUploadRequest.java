package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

import java.io.IOException;
import java.util.ArrayList;

@JsonTypeName("TCPUploadRequest")
public class TCPUploadRequest implements Request {
    private ArrayList<String> paths;
    private String username;
    private String token;
    private int numClient;

    public TCPUploadRequest(ArrayList<String> paths, String username, String token, int numClient) {
        this.paths = paths;
        this.username = username;
        this.token = token;
        this.numClient = numClient;
    }

    public TCPUploadRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public int getNumClient() {
        return numClient;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    @Override
    public Response run(RequestHandler requestHandler) throws IOException, ClassNotFoundException {
        return requestHandler.handleTCPUploadRequest(this);
    }
}
