package mr.demonid.storage.service.exceptions;

public class BadPhoneException extends BaseStorageException {

    public BadPhoneException(String message) {
        super("Ошибка БД 'Телефоны'", message);
    }
}
