package mr.demonid.storage.service.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.services.WorkScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для базы данных режимов работы.
 *
 * Все сервисы в случае ошибки генерируют исключение BadScheduleException,
 * которое возвращает HttpStatus.BAD_REQUEST
 *
 * Поскольку у нас права каждому пользователю назначает
 * непосредственно админ, то для каждого эндпоинта
 * указаны условия доступа, независимо от его роли.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/storage/schedule")
public class WorkScheduleController {

    private WorkScheduleService scheduleService;

    /**
     * Возвращает режим по его id
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-by-id")
    public ResponseEntity<WorkScheduleDTO> getById(@RequestParam(value = "id") Long id) {
        return ResponseEntity.ok(scheduleService.getById(id));
    }

    /**
     * Загрузка всех имеющихся в БД режимов.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-all")
    public ResponseEntity<List<WorkScheduleDTO>> getAll() {
        return ResponseEntity.ok(scheduleService.getAll());
    }

    /**
     * Добавляет новый режим работы.
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping
    public ResponseEntity<WorkScheduleDTO> create(@RequestBody WorkScheduleDTO request) {
        return ResponseEntity.ok(scheduleService.create(request));
    }

    /**
     * Обновление существующего режима.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping
    public ResponseEntity<WorkScheduleDTO> update(@RequestBody WorkScheduleDTO request) {
        return ResponseEntity.ok(scheduleService.update(request));
    }

    /**
     * Удаление режима.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam(value = "id") Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok().build();
    }

}
