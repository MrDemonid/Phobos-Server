package mr.demonid.storage.service.exceptions;

import lombok.Getter;


@Getter
public abstract class BaseStorageException extends RuntimeException {

    private final String title;

    public BaseStorageException(String title, String message) {
        super(message);
        this.title = title;
    }
}

