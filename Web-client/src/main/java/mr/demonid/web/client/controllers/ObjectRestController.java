package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.ObjectEntityDTO;
import mr.demonid.web.client.dto.PhoneDTO;
import mr.demonid.web.client.service.ObjectService;
import mr.demonid.web.client.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/web-service/object")
public class ObjectRestController {

    private final ObjectService objectService;
    private final PhoneService phoneService;

    /**
     * Добавление нового объекта.
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/new")
    ResponseEntity<ObjectEntityDTO> newPerson(@RequestBody ObjectEntityDTO objectEntityDTO) {
        return ResponseEntity.ok(objectService.createObject(objectEntityDTO));
    }

    /**
     * Изменение данных существующего объекта.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    ResponseEntity<Void> updateObject(@RequestBody ObjectEntityDTO objectEntityDTO) {
        objectService.updateObject(objectEntityDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление объекта.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteObject(@PathVariable Long id) {
        objectService.deleteObject(id);
        return ResponseEntity.ok().build();
    }


    /*====================================================================================
    Методы для работы с Object Details (привязка/удаления связей)
     =====================================================================================*/

    /**
     * Привязывает объекту график работы.
     * @param objectId   Идентификатор сотрудника.
     * @param scheduleId График работы.
     */
    @PostMapping("/{objectId}/link/schedule/{scheduleId}")
    public ResponseEntity<?> linkSchedule(@PathVariable Long objectId, @PathVariable Long scheduleId) {
        return returnJsonResponse(objectService.addSchedule(objectId, scheduleId));
    }

    /**
     * Удаляет график работы у объекта.
     * @param objectId   Номер объекта.
     * @param scheduleId График работы.
     */
    @PostMapping("/{objectId}/unlink/schedule/{scheduleId}")
    public ResponseEntity<?> unlinkSchedule(@PathVariable Long objectId, @PathVariable Long scheduleId) {
        return returnJsonResponse(objectService.delSchedule(objectId, scheduleId));
    }


    /**
     * Привязка телефона к объекту.
     * @param objectId Номер объекта.
     * @param phoneId  Идентификатор телефона.
     */
    @PostMapping("/{objectId}/link-phone/{phoneId}")
    public ResponseEntity<?> addPhoneToObject(@PathVariable Long objectId, @PathVariable Long  phoneId) {
        return returnJsonResponse(objectService.linkPhone(objectId, phoneId));
    }

    /**
     * Отвязка телефона от объекта.
     * @param objectId    Идентификатор сотрудника.
     * @param phoneId  Идентификатор телефона.
     */
    @PostMapping("/{objectId}/unlink-phone/{phoneId}")
    public ResponseEntity<?> unlinkPhoneToObject(@PathVariable Long objectId, @PathVariable Long  phoneId) {
        return returnJsonResponse(objectService.unlinkPhone(objectId, phoneId));
    }


    /**
     * Привязка к объекту ответственного лица.
     * @param objectId Номер объекта.
     * @param tabNo    Табельный номер сотрудника.
     */
    @PostMapping("/{objectId}/link-person/{tabNo}")
    public ResponseEntity<?> linkPersonToObject(@PathVariable Long objectId, @PathVariable Long  tabNo) {
        return returnJsonResponse(objectService.linkPerson(objectId, tabNo));
    }

    /**
     * Отвязка ответственного лица от объекта.
     * @param objectId Номер объекта.
     * @param tabNo    Табельный номер сотрудника.
     */
    @PostMapping("/{objectId}/unlink-person/{tabNo}")
    public ResponseEntity<?> unlinkPersonToObject(@PathVariable Long objectId, @PathVariable Long  tabNo) {
        return returnJsonResponse(objectService.unlinkPerson(objectId, tabNo));
    }



    // Если строка на входе не пустая, то считает её ошибкой и возвращает
    // в теле ответа, виде JSON, в поле "message"ю
    private ResponseEntity<?> returnJsonResponse(String msg) {
        if (msg.isBlank()) {
            return ResponseEntity.ok().body(msg);
        }
        // возвращаем JSON-ответ, с пояснением ошибки
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", msg));
    }

}
