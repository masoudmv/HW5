package shared.request;


import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

import java.io.IOException;

@JsonTypeName("LoginRequest")
public class LoginRequest implements Request{

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public Response run(RequestHandler requestHandler) throws IOException {
        return requestHandler.handleLoginRequest(this);
    }
}
