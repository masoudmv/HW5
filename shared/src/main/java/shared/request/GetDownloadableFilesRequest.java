package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;
import java.net.SocketException;

@JsonTypeName("GetDownloadableFilesRequest")
public class GetDownloadableFilesRequest implements Request {
    private String token;
    private String username;

    public GetDownloadableFilesRequest(String token, String username) {

        this.token = token;
        this.username = username;
    }

    public GetDownloadableFilesRequest() {
    }


    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public Response run(RequestHandler requestHandler) throws SocketException {
        return requestHandler.handleGetDownloadableFilesRequest(this);
    }

}
