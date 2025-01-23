package mr.demonid.transfer.service.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.transfer.service.dto.MessageRequest;
import mr.demonid.transfer.service.services.APMSender;
import mr.demonid.transfer.service.services.DevSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для обмена сообщениями с программой-клиентом (АРМ) и удаленными устройствами.
 */
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/transfer/awp")
public class ApiController {

    private final APMSender apmSender;
    private final DevSender devSender;

    /**
     * Пересылает сообщение клиенту.
     */
    @PostMapping("/send-to-client")
    public ResponseEntity<Void> resendToClient(@RequestBody MessageRequest message) {
        apmSender.addToQueue(message);
        return ResponseEntity.ok().build();
    }

    /**
     * Принимает сообщение от клиента и пересылает удаленному оборудованию.
     */
    @PostMapping("/send-to-dev")
    public ResponseEntity<Void> resendToDevice(@RequestBody MessageRequest message) {
        devSender.addToQueue(message);
        return ResponseEntity.ok().build();
    }

}
