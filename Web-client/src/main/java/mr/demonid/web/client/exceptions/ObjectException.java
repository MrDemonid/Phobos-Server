package mr.demonid.web.client.exceptions;

public class ObjectException extends ClientBaseException {

    public ObjectException(String message) {
        super("Ошибка ObjectEntity()", message);
    }
}
