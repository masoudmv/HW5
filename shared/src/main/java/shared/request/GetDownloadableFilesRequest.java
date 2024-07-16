package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;
import java.net.SocketException;

@JsonTypeName("GetDownloadableFilesRequest")
public class GetDownloadableFilesRequest implements Request {
    private String token;
    private String username;
    private int numClient;

    public GetDownloadableFilesRequest(String token, String username, int numClient) {
        this.token = token;
        this.username = username;
        this.numClient = numClient;
    }

    public GetDownloadableFilesRequest() {
    }


    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public int getNumClient() {
        return numClient;
    }

    @Override
    public Response run(RequestHandler requestHandler) throws SocketException {
        return requestHandler.handleGetDownloadableFilesRequest(this);
    }

}
