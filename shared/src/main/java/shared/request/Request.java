package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.response.Response;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
//        @JsonSubTypes.Type(value = HiRequest.class, name = "hiRequest"),
        @JsonSubTypes.Type(value = SignInRequest.class, name = "SignInRequest"),
        @JsonSubTypes.Type(value = LoginRequest.class, name = "LoginRequest"),
        @JsonSubTypes.Type(value = GetUploadedFilesRequest.class, name = "GetUploadedFilesRequest"),
        @JsonSubTypes.Type(value = AccessRequest.class, name = "AccessRequest"),
        @JsonSubTypes.Type(value = GetDownloadableFilesRequest.class, name = "GetDownloadableFilesRequest"),
})
public interface Request {
    Response run(RequestHandler requestHandler);
}

