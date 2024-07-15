package shared.request;

import shared.response.Response;

public interface RequestHandler {
//    Response handleHiRequest(HiRequest hiRequest);
    Response handleLoginRequest(LoginRequest loginRequest);
    Response handleSignInRequest(SignInRequest signInRequest);
    Response handleAccessRequest(AccessRequest accessRequest);
    Response handleGetUploadedFilesRequest(GetUploadedFilesRequest getUploadedFilesRequest);
    Response handleGetDownloadableFilesRequest(GetDownloadableFilesRequest getDownloadableFilesRequest);
}
