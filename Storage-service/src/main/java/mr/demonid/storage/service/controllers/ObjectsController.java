package mr.demonid.storage.service.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.dto.ObjectEntityDTO;
import mr.demonid.storage.service.dto.PageDTO;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.services.ObjectEntityService;
import mr.demonid.storage.service.services.filters.ObjectFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/storage/objects")
public class ObjectsController {

    ObjectEntityService objectService;

    /**
     * Добавление нового объекта.
     * Только для пользователей с правом 'write'
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/create")
    public ResponseEntity<ObjectEntityDTO> create(@RequestBody ObjectEntityDTO objectEntityDTO) {
        return ResponseEntity.ok(objectService.create(objectEntityDTO));
    }

    /**
     * Обновление данных об объекте.
     * Только для админов.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    public ResponseEntity<ObjectEntityDTO> update(@RequestBody ObjectEntityDTO objectEntityDTO) {
        return ResponseEntity.ok(objectService.update(objectEntityDTO));
    }

    /**
     * Удаление объекта.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        objectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Постраничная выборка базовых данных об объектах.
     * @param pageable Страница, с которой выбираем данные.
     */
    @PostMapping("/get-short-list/filter")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public ResponseEntity<PageDTO<ObjectEntityDTO>> getAllPageableWithFilter(@RequestBody ObjectFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(objectService.getAllPageableWithFilter(filter, pageable)));
    }


    /*======================================================================
        Методы для привязки/отвязки связанных данных объекту
     ======================================================================*/

    /**
     * Получение подробных данных о пользователе.
     * @param objectId Номер объекта.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-by-id/{objectId}")
    public ResponseEntity<ObjectEntityDTO> getById(@PathVariable Long objectId) {
        return ResponseEntity.ok(objectService.getObjectById(objectId));
    }


    /**
     * Привязка расписания работы объекту.
     * @param objectId   Номер объекта.
     * @param scheduleId Идентификатор расписания.
     */
    @PostMapping("/link/schedule/{objectId}/{scheduleId}")
    public ResponseEntity<ObjectEntityDTO> linkSchedule(@PathVariable Long objectId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(objectService.addSchedule(objectId, scheduleId));
    }

    /**
     * Отвязка расписания работы от пользователя.
     * @param objectId   Номер объекта.
     * @param scheduleId Идентификатор расписания.
     */
    @PostMapping("/unlink/schedule/{objectId}/{scheduleId}")
    public ResponseEntity<ObjectEntityDTO> unlinkSchedule(@PathVariable Long objectId, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(objectService.delSchedule(objectId, scheduleId));
    }


    /**
     * Привязка телефона к объекту.
     * @param objectId Номер объекта.
     * @param phoneId  Идентификатор телефона.
     */
    @PostMapping("/link/phone/{objectId}/{phoneId}")
    public ResponseEntity<ObjectEntityDTO> linkPhone(@PathVariable Long objectId, @PathVariable Long phoneId) {
        return ResponseEntity.ok(objectService.linkPhone(objectId, phoneId));
    }

    /**
     * Отвязка телефона от сотрудника.
     * @param objectId Номер объекта.
     * @param phoneId  Идентификатор телефона.
     */
    @PostMapping("/unlink/phone/{objectId}/{phoneId}")
    public ResponseEntity<ObjectEntityDTO> unlinkPhone(@PathVariable Long objectId, @PathVariable Long phoneId) {
        return ResponseEntity.ok(objectService.unlinkPhone(objectId, phoneId));
    }


    /**
     * Добавление к объекту ответственного лица.
     * @param objectId Номер объекта.
     * @param tabNo    Табельный номер сотрудника.
     */
    @PostMapping("/link/person/{objectId}/{tabNo}")
    public ResponseEntity<ObjectEntityDTO> linkPerson(@PathVariable Long objectId, @PathVariable Long tabNo) {
        return ResponseEntity.ok(objectService.linkPerson(objectId, tabNo));
    }

    /**
     * Отвязка ответственного лица от объекта.
     * @param objectId Номер объекта.
     * @param tabNo    Табельный номер сотрудника.
     */
    @PostMapping("/unlink/person/{objectId}/{tabNo}")
    public ResponseEntity<ObjectEntityDTO> unlinkPerson(@PathVariable Long objectId, @PathVariable Long tabNo) {
        return ResponseEntity.ok(objectService.unlinkPerson(objectId, tabNo));
    }


}
