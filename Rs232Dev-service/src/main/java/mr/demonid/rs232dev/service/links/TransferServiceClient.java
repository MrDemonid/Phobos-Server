package mr.demonid.rs232dev.service.links;

import mr.demonid.rs232dev.service.dto.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TRANSFER-SERVICE")
public interface TransferServiceClient {

    /**
     * Отсылка данных через TRANSFER к приложению-клиенту
     */
    @PostMapping("/api/transfer/awp/send-to-client")
    ResponseEntity<String> transferAWP(@RequestBody MessageRequest message);


}
