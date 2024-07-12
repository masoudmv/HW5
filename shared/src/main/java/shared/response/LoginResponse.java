package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LoginResponse")
public class LoginResponse implements Response{

    private boolean successful;

    public LoginResponse(boolean successful) {
        this.successful = successful;
    }

    public LoginResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleLogInResponse(this);
    }
}
