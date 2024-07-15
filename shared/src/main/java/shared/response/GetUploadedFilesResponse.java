package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

@JsonTypeName("GetUploadedFilesResponse")
public class GetUploadedFilesResponse implements Response{

    private boolean valid;
    private ArrayList<String> filesNames = new ArrayList<>();

    public GetUploadedFilesResponse(boolean valid, ArrayList<String> filesNames) {
        this.valid = valid;
        this.filesNames = filesNames;
    }

    public GetUploadedFilesResponse() {
    }

    public GetUploadedFilesResponse(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public ArrayList<String> getFilesNames() {
        return filesNames;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleGetUploadedFilesResponse(this);
    }
}
