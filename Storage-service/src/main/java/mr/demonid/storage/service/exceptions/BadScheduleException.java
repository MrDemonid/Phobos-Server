package mr.demonid.storage.service.exceptions;

public class BadScheduleException extends BaseStorageException {

    public BadScheduleException(String message) {
        super("Ошибка schedule()", message);
    }
}
