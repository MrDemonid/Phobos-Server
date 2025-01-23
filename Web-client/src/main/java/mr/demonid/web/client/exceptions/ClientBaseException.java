package mr.demonid.web.client.exceptions;

import lombok.Getter;

@Getter
public abstract class ClientBaseException extends RuntimeException {

    private String title;

    public ClientBaseException(String title, String message) {
        super(message);
        this.title = title;
    }

}
