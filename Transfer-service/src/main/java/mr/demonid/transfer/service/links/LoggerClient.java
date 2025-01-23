package mr.demonid.transfer.service.links;

import mr.demonid.transfer.service.dto.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "LOGGER-SERVICE")
public interface LoggerClient {

    /**
     * Сохранение операции в базе данных
     */
    @PostMapping("/api/logger/store")
    ResponseEntity<Long> storeLogger(@RequestBody MessageRequest operation);

    /**
     * Подтвержджение операции.
     * @param id Уникальный идентификатор записи.
     */
    @GetMapping("/api/logger/confirm")
    ResponseEntity<Void> confirm(@RequestParam Long id);

}
