package mr.demonid.transfer.service.services;

import lombok.extern.log4j.Log4j2;
import mr.demonid.transfer.service.config.AppConfig;
import mr.demonid.transfer.service.config.SecurityProperties;
import mr.demonid.transfer.service.dto.MessageRequest;
import mr.demonid.transfer.service.dto.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * Поток пересылки сообщений оператору АРМ.
 */
@Component
@Log4j2
public class APMSender extends Sender {

    private final RestTemplate restTemplate;
    private final InformationService informationService;

    public APMSender(LoggerService loggerService, SecurityProperties properties, AppConfig appConfig, InformationService informationService) {
        super("APM", loggerService, properties, appConfig, appConfig.getApmBufSize() <= 0 ? 200 : appConfig.getApmBufSize());
        restTemplate = new RestTemplate();
        this.informationService = informationService;
        start();
    }

    /**
     * Отсылает пакет оператору, а также уведомление подписчикам.
     */
    @Override
    protected ResponseEntity<Void> sendMessage(MessageRequest message) {
        try {
            ResponseEntity<Void> res = restTemplate.postForEntity(properties.getOuter().getUrl(), message, Void.class);
            NotificationRequest notify = new NotificationRequest(
                    message.getRepeater(),
                    message.getKey(),
                    message.getCode(),
                    message.getLine(),
                    message.getType(),
                    message.getDate(),
                    message.getId() != null,
                    res.getStatusCode().is2xxSuccessful()
            );
            informationService.sendMessage(notify);
            return res;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

}

