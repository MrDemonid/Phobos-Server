package mr.demonid.rs232dev.service.dto;

import lombok.Data;

@Data
public class CustomTokenResponse {

    private String access_token;
    private String scope;
    private String token_type;
    private int expires_in;


}
