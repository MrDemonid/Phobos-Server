package mr.demonid.transfer.service.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.transfer.service.dto.MessageRequest;
import mr.demonid.transfer.service.dto.NotificationRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class InformationService {
    private AmqpTemplate amqpTemplate;

    /**
     * Отправляем сообщение в очередь.
     */
    public void sendMessage(NotificationRequest message) {
        try {
            amqpTemplate.convertAndSend("PhobosNotification", "", message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
