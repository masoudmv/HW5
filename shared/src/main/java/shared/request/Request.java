package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.response.Response;

import java.io.IOException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
//        @JsonSubTypes.Type(value = HiRequest.class, name = "hiRequest"),
        @JsonSubTypes.Type(value = SignInRequest.class, name = "SignInRequest"),
        @JsonSubTypes.Type(value = LoginRequest.class, name = "LoginRequest"),
        @JsonSubTypes.Type(value = GetUploadedFilesRequest.class, name = "GetUploadedFilesRequest"),
        @JsonSubTypes.Type(value = TCPUploadRequest.class, name = "TCPUploadRequest"),
        @JsonSubTypes.Type(value = GetDownloadableFilesRequest.class, name = "GetDownloadableFilesRequest"),
        @JsonSubTypes.Type(value = AccessRequest.class, name = "AccessRequest"),

})
public interface Request {
    Response run(RequestHandler requestHandler) throws IOException, ClassNotFoundException;
}

