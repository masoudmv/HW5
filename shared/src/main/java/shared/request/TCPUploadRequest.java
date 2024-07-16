package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

import java.io.IOException;

@JsonTypeName("TCPUploadRequest")
public class TCPUploadRequest implements Request {
    private String username;
    private String token;
    private int numClient;

    public TCPUploadRequest(String username, String token, int numClient) {
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

    @Override
    public Response run(RequestHandler requestHandler) throws IOException, ClassNotFoundException {
        return requestHandler.handleTCPUploadRequest(this);
    }
}
