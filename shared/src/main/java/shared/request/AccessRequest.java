package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("AccessRequest")
public class AccessRequest implements Request{
    String username;
    private String fileName;

    public AccessRequest(String username, String fileName) {
        this.username = username;
        this.fileName = fileName;
    }

    public AccessRequest() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleAccessRequest(this);
    }
}
