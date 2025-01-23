package mr.demonid.transfer.service.services;

import lombok.extern.log4j.Log4j2;
import mr.demonid.transfer.service.config.AppConfig;
import mr.demonid.transfer.service.config.SecurityProperties;
import mr.demonid.transfer.service.dto.MessageRequest;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;


/**
 * Абстрактный класс буферизированной отправки сообщений.
 */
@Log4j2
public abstract class Sender {
    private final String name;

    protected final LinkedBlockingDeque<MessageRequest> messageQueue;
    private final Thread sendThread;
    protected volatile boolean running = true;
    protected final LoggerService loggerService;

    protected final AppConfig appConfig;
    protected final SecurityProperties properties;


    public Sender(String name, LoggerService loggerService, SecurityProperties properties, AppConfig appConfig, int size) {
        this.name = name;
        this.loggerService = loggerService;
        this.properties = properties;
        this.appConfig = appConfig;
        this.messageQueue = new LinkedBlockingDeque<>(size <= 0 ? 100 : size);
        // Инициализация потока обработки очереди
        this.running = false;
        sendThread = new Thread(this::threadSend);
    }

    /**
     * Запуск потока.
     */
    public void start() {
        log.info("Поток {} начал работу", name);
        running = true;
        sendThread.start();
    }

    /**
     * Завершение работы.
     */
    public void shutdown() {
        log.info("Поток {} завершает работу", name);
        running = false;
        sendThread.interrupt();
    }

    /**
     * Добавление сообщения в очередь.
     */
    public void addToQueue(MessageRequest message) {
        if (!messageQueue.offerLast(message)) {
            try {
                messageQueue.removeFirst();
                messageQueue.addLast(message);
            } catch (NoSuchElementException ignored) {}
        }
    }

    /*
        Поток.
     */
    private void threadSend() {
        while (running) {
            try {
                MessageRequest message = messageQueue.takeFirst();
                if (!doSendMessage(message)) {
                    messageQueue.offerFirst(message);       // потом попробуем снова.
                    // подождем, может ошибка разрешится
                    Thread.sleep(appConfig.getWaitTime() == 0 ? 1000 : appConfig.getWaitTime());
                }
            } catch (InterruptedException e) {
                // сохраняем флаг прерывания и выходим из цикла
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // просто игнорируем и продолжаем цикл
                log.error("threadSend{}(). Ошибка пересылки сообщения: {}", name, e.getMessage());
            }
        }
        log.info("Поток {} остановлен", name);
    }

    /*
        Попытка отправки сообщения.
     */
    private boolean doSendMessage(MessageRequest message) {
        try {
            if (message.getId() == null) {
                // отсылаем в Logger
                Long id = loggerService.storeToLogger(message);
                message.setId(id);
            }
            // Отправляем сообщение устройству
            ResponseEntity<Void> response = sendMessage(message);
            if (response.getStatusCode().is2xxSuccessful()) {
                if (message.getId() != null) {
                    loggerService.messageConfirm(message.getId());
                }
                return true;
            }
        } catch (Exception e) {
            log.error("sendMessage{}(). Ошибка пересылки сообщения: {}", name, e.getMessage());
        }
        return false;
    }

    /**
     * Собственно это и есть отсылка сообщения.
     */
    protected abstract ResponseEntity<Void> sendMessage(MessageRequest message);

}
