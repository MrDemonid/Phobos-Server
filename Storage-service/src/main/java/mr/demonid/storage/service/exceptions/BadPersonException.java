package mr.demonid.storage.service.exceptions;

public class BadPersonException extends BaseStorageException {
    public BadPersonException(String message) {
        super("Ошибка БД 'Сотрудники'", message);
    }
}

