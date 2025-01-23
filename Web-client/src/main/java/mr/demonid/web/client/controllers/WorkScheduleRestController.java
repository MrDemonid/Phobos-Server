package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.WorkScheduleDTO;
import mr.demonid.web.client.service.WorkScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер для WorkSchedule.
 * Поскольку на стороне браузера основные функции реализованы
 * через Java Script, то для удобства взаимодействия этот
 * контроллер реализован в виде REST-контроллера.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/web-service/work-schedule")
public class WorkScheduleRestController {

    WorkScheduleService workScheduleService;

    /**
     * Создание нового расписания.
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/new")
    public ResponseEntity<WorkScheduleDTO> addNewSchedule(@RequestBody WorkScheduleDTO workScheduleDTO) {
        WorkScheduleDTO newSchedule = workScheduleService.createWorkSchedule(workScheduleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSchedule);
    }

    /**
     * Изменение существующего расписания.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    public ResponseEntity<Void> updateWorkSchedule(@RequestBody WorkScheduleDTO workScheduleDTO) {
        WorkScheduleDTO schedule = workScheduleService.updateWorkSchedule(workScheduleDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление расписания по его id.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWorkSchedule(@PathVariable Long id) {
        workScheduleService.deleteWorkSchedule(id);
        return ResponseEntity.ok().build();
    }


}
