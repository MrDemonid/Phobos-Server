package mr.demonid.notification.service.service;

import mr.demonid.notification.service.domain.NotifyTarget;
import mr.demonid.notification.service.domain.NotifyType;
import mr.demonid.notification.service.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Сервис рассылки уведомлений по мылу.
 */
@Service
public class NotifyEmail {

    private static final Logger log = LoggerFactory.getLogger(NotifyEmail.class);

    private final EmailService emailService;

    public NotifyEmail(EmailService emailService) {
        this.emailService = emailService;
    }


    public void notifyEmail(NotifyTarget target, NotificationRequest message) {
        NotifyType msgType = NotifyType.fromCode(message.getCode());
        if (msgType == target.getNotificationType()) {
            log.info("Sending email to {}, body: {}", target.getEmail(), message);
            String text = "Information: " + target.getNotificationType().toString() + ", message: " + message;
            emailService.sendEmail(target.getEmail(), "Alarm notification " + target.getKey() + ":" + target.getNotificationType(), text);
        }
    }

}
