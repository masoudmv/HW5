package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("GetDownloadableFilesRequest")
public class GetDownloadableFilesRequest implements Request {
    String token;
    String userName;

    public GetDownloadableFilesRequest(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public GetDownloadableFilesRequest() {
    }


    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }


    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleGetDownloadableFilesRequest(this);
    }

}
