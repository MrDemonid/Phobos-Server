package mr.demonid.transfer.service.dto;

import lombok.Data;

@Data
public class CustomTokenResponce {

    private String access_token;
    private String scope;
    private String token_type;
    private long expires_in;


}
