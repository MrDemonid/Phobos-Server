package mr.demonid.web.client.exceptions;

public class WorkScheduleException extends ClientBaseException {

    public WorkScheduleException(String message) {
        super("Ошибка schedule()", message);
    }

}
