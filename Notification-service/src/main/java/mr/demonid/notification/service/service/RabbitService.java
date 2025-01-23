package mr.demonid.notification.service.service;

import mr.demonid.notification.service.dto.NotificationRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Слушатель очереди "PhobosQueueNotification".
 * Просто передает принятые сообщения на рассылку.
 */
@Service
public class RabbitService {
    private final NotifyService notifyService;

    public RabbitService(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    /**
     * Прием информационного сообщения от другого микросервиса.
     */
    @RabbitListener(queues = "PhobosQueueNotification")
    public void handleBusMessage(NotificationRequest message) {
        notifyService.broadcast(message);
    }
}

