package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("SignInRequest")
public class SignInRequest implements Request{
    private String username;
    private String password;

    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SignInRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleSignInRequest(this);
    }
}
