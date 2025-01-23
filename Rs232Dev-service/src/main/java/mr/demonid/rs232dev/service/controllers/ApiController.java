package mr.demonid.rs232dev.service.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.RepeaterRs232;
import mr.demonid.rs232dev.service.dto.MessageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/rs232dev")
public class ApiController {

    private RepeaterRs232 repeaterRs232;

    /**
     * Прием сообщения от оператора (точнее от Transfer-service)
     * и его отправка на устройство.
     */
    @PostMapping("/receive")
    public ResponseEntity<Void> receive(@RequestBody MessageRequest request) {
        if (repeaterRs232.send(request.getRepeater(), request.getKey(), request.getCode())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
