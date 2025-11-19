package Request;

import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String phone;
    private String password;
}
