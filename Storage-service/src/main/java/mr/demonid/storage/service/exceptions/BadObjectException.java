package mr.demonid.storage.service.exceptions;

public class BadObjectException extends BaseStorageException {
    public BadObjectException(String message) {
        super("Ошибка БД 'Объекты'", message);
    }
}
