package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LoginResponse")
public class LoginResponse implements Response{

    private boolean successful;
    private String token;
    private String userName;

    public LoginResponse(boolean successful, String token, String userName) {
        this.successful = successful;
        this.token = token;
        this.userName = userName;
    }

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isSuccessful() {
        return successful;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleLogInResponse(this);
    }
}
