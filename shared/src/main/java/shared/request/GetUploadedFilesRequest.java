package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("GetUploadedFilesRequest")
public class GetUploadedFilesRequest implements Request{
    private String token;
    private String userName;

    public GetUploadedFilesRequest(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public GetUploadedFilesRequest() {
    }


    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleGetUploadedFilesRequest(this);
    }
}
