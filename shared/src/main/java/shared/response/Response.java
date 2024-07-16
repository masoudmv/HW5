package shared.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.request.LoginRequest;

import java.net.SocketException;
import java.net.UnknownHostException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = HiResponse.class, name = "hiResponse"),
        @JsonSubTypes.Type(value = SignInResponse.class, name = "SignInResponse"),
        @JsonSubTypes.Type(value = LoginResponse.class, name = "LoginResponse"),
        @JsonSubTypes.Type(value = GetUploadedFilesResponse.class, name = "GetUploadedFilesResponse"),
        @JsonSubTypes.Type(value = GetDownloadableFilesResponse.class, name = "GetDownloadableFilesResponse"),
        @JsonSubTypes.Type(value = TCPUploadResponse.class, name = "TCPUploadResponse"),

})
public interface Response {
    void run(ResponseHandler responseHandler) throws SocketException, UnknownHostException;
}
