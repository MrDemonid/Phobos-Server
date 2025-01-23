package mr.demonid.rs232dev.service.exceptions;

public class BadSendMessageException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Ошибка отправки сообщения: клиент не на связи.";
    }
}
