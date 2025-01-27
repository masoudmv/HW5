package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SignInResponse")
public class SignInResponse implements Response{
    private boolean successful;

    public SignInResponse(boolean successful) {
        this.successful = successful;
    }

    public SignInResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void run(ResponseHandler responseHandler) {
        responseHandler.handleSignInResponse(this);
    }
}
