package shared.response;

import shared.request.GetDownloadableFilesRequest;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleSignInResponse(SignInResponse signInResponse);
    void handleLogInResponse(LoginResponse loginResponse);
    void handleGetUploadedFilesResponse(GetUploadedFilesResponse getUploadedFilesResponse);
    void handleGetDownloadableFilesResponse(GetDownloadableFilesResponse getDownloadableFilesResponse);

}
