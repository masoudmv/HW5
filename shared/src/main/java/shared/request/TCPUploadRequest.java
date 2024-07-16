package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

import java.io.IOException;

@JsonTypeName("TCPUploadRequest")
public class TCPUploadRequest implements Request {
    private String username;
    private String token;

    public TCPUploadRequest(String user, String token) {
        this.username = user;
        this.token = token;
    }

    public TCPUploadRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Response run(RequestHandler requestHandler) throws IOException, ClassNotFoundException {
        return requestHandler.handleTCPUploadRequest(this);
    }
}
