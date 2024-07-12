package shared.response;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleSignInResponse(SignInResponse signInResponse);

}
