package mr.demonid.transfer.service.services;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * Класс для корректного закрытия программы.
 */
@Component
@AllArgsConstructor
@Log4j2
public class AppShutdown {

    private final APMSender apmSender;
    private final DevSender devSender;

    @PreDestroy
    public void onShutdown() {
        log.info("Приложение завершает работу.");
        apmSender.shutdown();
        devSender.shutdown();
    }
}

