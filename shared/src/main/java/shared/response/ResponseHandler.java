package shared.response;

import shared.request.GetDownloadableFilesRequest;

import java.net.SocketException;
import java.net.UnknownHostException;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleSignInResponse(SignInResponse signInResponse);
    void handleLogInResponse(LoginResponse loginResponse);
    void handleGetUploadedFilesResponse(GetUploadedFilesResponse getUploadedFilesResponse);
    void handleGetDownloadableFilesResponse(GetDownloadableFilesResponse getDownloadableFilesResponse);
    void handleTCPUploadResponse(TCPUploadResponse tcpUploadResponse) throws UnknownHostException, SocketException;

}
