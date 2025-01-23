package mr.demonid.transfer.service.services;

import feign.FeignException;
import lombok.extern.log4j.Log4j2;
import mr.demonid.transfer.service.config.AppConfig;
import mr.demonid.transfer.service.config.SecurityProperties;
import mr.demonid.transfer.service.dto.MessageRequest;
import mr.demonid.transfer.service.links.Rs232DevClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Поток пересылки сообщений на удаленные устройства.
 * Пока еще только через RS232.
 */
@Component
@Log4j2
public class DevSender extends Sender {

    private final Rs232DevClient rs232DevClient;

    public DevSender(LoggerService loggerService, Rs232DevClient rs232DevClient, SecurityProperties properties, AppConfig appConfig) {
        super("Device", loggerService, properties, appConfig, appConfig.getDevBufSize() <= 0 ? 1000 : appConfig.getDevBufSize());
        this.rs232DevClient = rs232DevClient;
        start();
    }

    @Override
    protected ResponseEntity<Void> sendMessage(MessageRequest message) {
        try {
            return rs232DevClient.sendToDev(message);
        } catch (FeignException e) {
            return ResponseEntity.badRequest().build();
//            return ResponseEntity.status(e.status()).body(null);
        }
    }
}

