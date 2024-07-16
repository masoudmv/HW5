package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@JsonTypeName("GetDownloadableFilesResponse")
public class GetDownloadableFilesResponse implements Response{

    private boolean invalid;
    private HashMap<String, Boolean> files = new HashMap<>();

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



    @Override
    public void run(ResponseHandler responseHandler) throws IOException, ClassNotFoundException {
        responseHandler.handleGetDownloadableFilesResponse(this);
    }
}
