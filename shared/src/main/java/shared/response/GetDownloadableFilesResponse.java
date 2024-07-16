package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

@JsonTypeName("GetDownloadableFilesResponse")
public class GetDownloadableFilesResponse implements Response{
    private String username;
    private String token;
    private boolean invalid;
    private HashMap<String, Boolean> files = new HashMap<>();


    public GetDownloadableFilesResponse(String username, String token, boolean invalid, HashMap<String, Boolean> files) {
        this.username = username;
        this.token = token;
        this.invalid = invalid;
        this.files = files;
    }

    public GetDownloadableFilesResponse(boolean invalid, HashMap<String, Boolean> files) {
        this.invalid = invalid;
        this.files = files;
    }

    public GetDownloadableFilesResponse(boolean invalid) {
        this.invalid = invalid;
    }

    public GetDownloadableFilesResponse() {
    }

    public HashMap<String, Boolean> getFiles() {
        return files;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    @Override
    public void run(ResponseHandler responseHandler) throws IOException, ClassNotFoundException {
        responseHandler.handleGetDownloadableFilesResponse(this);
    }
}
