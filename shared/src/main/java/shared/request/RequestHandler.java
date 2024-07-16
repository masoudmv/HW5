package shared.request;

import shared.response.Response;

import java.io.IOException;

public interface RequestHandler {
//    Response handleHiRequest(HiRequest hiRequest);
    Response handleLoginRequest(LoginRequest loginRequest);
    Response handleSignInRequest(SignInRequest signInRequest);
    Response handleGetUploadedFilesRequest(GetUploadedFilesRequest getUploadedFilesRequest);
    Response handleTCPUploadRequest(TCPUploadRequest tcpUploadRequest) throws IOException, ClassNotFoundException;
    Response handleGetDownloadableFilesRequest(GetDownloadableFilesRequest getDownloadableFilesRequest);
    Response handleAccessRequest(AccessRequest accessRequest);

}
