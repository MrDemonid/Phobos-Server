package mr.demonid.rs232dev.service.services;

import feign.FeignException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import mr.demonid.RepeaterRs232;
import mr.demonid.TransferMessage;
import mr.demonid.rs232dev.service.config.AppProperties;
import mr.demonid.rs232dev.service.dto.MessageRequest;
import mr.demonid.rs232dev.service.links.TransferServiceClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
public class ReceiverService {

    private final RepeaterRs232 repeaterRs232;
    private final ExecutorService executorService;
    private final TransferServiceClient transferServiceClient;
    private final AppProperties appProperties;


    public ReceiverService(RepeaterRs232 repeaterRs232, TransferServiceClient transferServiceClient, AppProperties appProperties) {
        this.repeaterRs232 = repeaterRs232;
        this.transferServiceClient = transferServiceClient;
        this.executorService = Executors.newSingleThreadExecutor();
        this.appProperties = appProperties;
    }


    @PostConstruct
    public void startReceive() {
        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    TransferMessage msg = repeaterRs232.receive();
                    // Проверяем корректность пришедших данных и переправляем их адресату
                    if (msg != null && msg.getRepeater() > 0 && msg.getRepeater() <= appProperties.getNumRepeaters()) {
                        MessageRequest messageRequest = new MessageRequest(msg.getRepeater(), msg.getKey(), msg.getCode(), msg.getLine(), msg.getType(), LocalDateTime.now());
                        try {
                            transferServiceClient.transferAWP(messageRequest);
                        } catch (FeignException fe) {
                            log.error("Ошибка отправки сообщения получателю: {}", fe.getMessage());
                        }
                    } else {
                        log.error("Получены некорректные данные. Поток ожидает устранения проблемы.");
                        Thread.sleep(appProperties.getWaitTime());
                    }
                } catch (InterruptedException e) {
                    System.out.println("-- thread interrupted");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.error("Ошибка потока: {}", e.getMessage());
                }
            }
        });
    }


    @PreDestroy
    public void stopReceive() {
        repeaterRs232.close();
        executorService.shutdownNow();          // Пробуем немедленно завершить поток.
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                log.error("Не удалось завершить потоки за 5 секунд.");
            }
        } catch (InterruptedException e) {
            log.error("Ожидание завершения потоков было прервано.");
            Thread.currentThread().interrupt();
        }
        log.info("Поток чтения остановлен");
    }

}
