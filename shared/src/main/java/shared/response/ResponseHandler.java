package shared.response;

import shared.request.GetDownloadableFilesRequest;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleSignInResponse(SignInResponse signInResponse);
    void handleLogInResponse(LoginResponse loginResponse);
    void handleGetUploadedFilesResponse(GetUploadedFilesResponse getUploadedFilesResponse);
    void handleGetDownloadableFilesResponse(GetDownloadableFilesResponse getDownloadableFilesResponse) throws IOException, ClassNotFoundException;
    void handleTCPUploadResponse(TCPUploadResponse tcpUploadResponse) throws UnknownHostException, SocketException;

}
