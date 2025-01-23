package mr.demonid.transfer.service.services;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.transfer.service.dto.MessageRequest;
import mr.demonid.transfer.service.links.LoggerClient;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Log4j2
public class LoggerService {

    LoggerClient loggerClient;

    /**
     * Отправка сообщения в базу данных Logger.
     */
    public Long storeToLogger(MessageRequest msg) {
        try {
            return loggerClient.storeLogger(msg).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return null;
    }

    public void messageConfirm(Long id) {
        try {
            loggerClient.confirm(id);
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }
}
