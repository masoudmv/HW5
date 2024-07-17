package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SplittableRandom;

@JsonTypeName("GetDownloadableFilesResponse")
public class GetDownloadableFilesResponse implements Response{
    private boolean valid;
    private ArrayList<String> filesList = new ArrayList<>();

    public GetDownloadableFilesResponse(boolean valid, ArrayList<String> filesList) {
        this.valid = valid;
        this.filesList = filesList;
    }

    public GetDownloadableFilesResponse(boolean valid) {
        this.valid = valid;
    }

    public GetDownloadableFilesResponse() {
    }

    public ArrayList<String> getFilesList() {
        return filesList;
    }

    public boolean isValid() {
        return valid;
    }


    @Override
    public void run(ResponseHandler responseHandler) throws IOException, ClassNotFoundException {
        responseHandler.handleGetDownloadableFilesResponse(this);
    }
}
