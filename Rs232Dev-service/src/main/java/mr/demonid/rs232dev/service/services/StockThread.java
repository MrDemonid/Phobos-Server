package mr.demonid.rs232dev.service.services;

import feign.FeignException;
import mr.demonid.rs232dev.service.dto.MessageRequest;
import mr.demonid.rs232dev.service.dto.TMessage;
import mr.demonid.rs232dev.service.links.TransferServiceClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс для теста пересылки сообщений от сервера к клиентам.
 */
@Component
public class StockThread {

    Stack<TMessage> messages = new Stack<>() {{
        push(new TMessage(LocalDateTime.now(), 1, 103, 119, "no desc"));
        push(new TMessage(LocalDateTime.now(), 1, 103, 125, "no desc"));
        push(new TMessage(LocalDateTime.now(), 1,  50, 119, "no desc"));
        push(new TMessage(LocalDateTime.now(), 1,  20,  97, "no desc"));
        push(new TMessage(LocalDateTime.now(), 1, 103, 119, "no desc"));
    }};

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    TransferServiceClient transferServiceClient;

    public StockThread(TransferServiceClient transferServiceClient) {
        this.transferServiceClient = transferServiceClient;
//        executor.schedule(this::changePrice, 10, TimeUnit.SECONDS);
    }

    private void changePrice() {
        if (!messages.empty()) {
            TMessage msg = messages.pop();
            System.out.println("send message: '" + msg + "'");
            try {
                MessageRequest m = new MessageRequest(msg.getRepeater(), msg.getKey(), msg.getCode(), 0, 0, LocalDateTime.now());
                transferServiceClient.transferAWP(m);
                System.out.println("--> send Ok!");
            } catch (FeignException e) {
                System.out.println("--> Can`t send message: " + e.contentUTF8());
            }
            executor.schedule(this::changePrice, 5, TimeUnit.SECONDS);
        }
    }

}
