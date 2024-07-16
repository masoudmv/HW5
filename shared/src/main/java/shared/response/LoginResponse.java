package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LoginResponse")
public class LoginResponse implements Response{

    private boolean successful;
    private String token;
    private String userName;
    private int numClient;

    public LoginResponse(boolean successful, String token, String userName, int numClient) {
        this.successful = successful;
        this.token = token;
        this.userName = userName;
        this.numClient = numClient;
    }

    public LoginResponse() {
    }

    public int getNumClient() {
        return numClient;
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
