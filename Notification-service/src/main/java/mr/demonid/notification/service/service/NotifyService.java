package mr.demonid.notification.service.service;

import mr.demonid.notification.service.domain.NotifyTarget;
import mr.demonid.notification.service.dto.NotificationRequest;
import mr.demonid.notification.service.repository.NotifyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifyService {

    private final NotifyRepository notifyRepository;
    private final NotifyEmail notifyEmail;

    public NotifyService(NotifyRepository notifyRepository, NotifyEmail notifyEmail) {
        this.notifyRepository = notifyRepository;
        this.notifyEmail = notifyEmail;
    }

    public void broadcast(NotificationRequest message) {
        List<NotifyTarget> targets = notifyRepository.findAllByKey(message.getKey());
        targets.forEach(t -> {
            notifyEmail.notifyEmail(t, message);
        });

    }

}
