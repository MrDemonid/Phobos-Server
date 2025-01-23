package mr.demonid.logger.service.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.logger.service.config.AppProperties;
import mr.demonid.logger.service.dto.LogOperationRequest;
import mr.demonid.logger.service.dto.PageDTO;
import mr.demonid.logger.service.services.LogService;
import mr.demonid.logger.service.services.filter.LogFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/logger")
public class ApiController {

    LogService logService;

    /**
     * Прием данных для сохранения в БД.
     * Возвращает ID записи, чтобы другой сервис впоследствии смог поменять
     * статус записи с PENDING на SENT, т.е. подтвердить, что сообщение
     * дошло до адресата.
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/store")
    public ResponseEntity<Long> receive(@RequestBody LogOperationRequest log) {
        return ResponseEntity.ok(logService.putLog(log));
    }

    /**
     * Подтверждение записи.
     * Меняем статус записи с PENDING на SENT
     * @param id Идентификатор записи
     */
    @GetMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestParam Long id) {
        logService.confirm(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Выборка записей из БД.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @PostMapping("/read")
    public ResponseEntity<PageDTO<LogOperationRequest>> getLog(@RequestBody LogFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(logService.getLogs(filter, pageable)));
    }

}
