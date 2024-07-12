package shared.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.request.LoginRequest;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = HiResponse.class, name = "hiResponse"),
        @JsonSubTypes.Type(value = SignInResponse.class, name = "SignInResponse"),
        @JsonSubTypes.Type(value = LoginResponse.class, name = "LoginResponse"),

})
public interface Response {
    void run(ResponseHandler responseHandler);
}
