package mr.demonid.web.client.exceptions;

public class PersonException extends ClientBaseException {
    public PersonException(String message) {
        super("Ошибка Person()", message);
    }
}
