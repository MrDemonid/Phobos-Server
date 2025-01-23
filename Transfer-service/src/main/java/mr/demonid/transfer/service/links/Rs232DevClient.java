package mr.demonid.transfer.service.links;

import mr.demonid.transfer.service.dto.MessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "RS232DEV-SERVICE")
public interface Rs232DevClient {

    @PostMapping("/api/rs232dev/receive")
    ResponseEntity<Void> sendToDev(@RequestBody MessageRequest messageRequest);


}
