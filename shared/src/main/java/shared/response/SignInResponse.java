package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SignInResponse")
public class SignInResponse implements Response{
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleSignInResponse(this);
        System.out.println("SignIn message was sent");
    }
}
