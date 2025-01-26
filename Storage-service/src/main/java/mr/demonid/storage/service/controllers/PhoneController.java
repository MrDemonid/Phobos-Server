package mr.demonid.storage.service.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.services.PhoneService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для базы данных телефонов.
 *
 * Поскольку у нас права каждому пользователю назначает
 * непосредственно админ, то для каждого эндпоинта
 * указаны условия доступа, независимо от его роли.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/storage/phones")
public class PhoneController {

    private final PhoneService phoneService;

    /**
     * Возвращает телефон по его id.
     * Доступна всем авторизированным пользователям, с правом 'read'.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-by_id")
    public ResponseEntity<PhoneDTO> getById(@RequestParam long id) {
        return ResponseEntity.ok(phoneService.getById(id));
    }

    /**
     * Возвращает список телефонов, выбранных по заданным параметрам.
     * Доступна всем авторизированным пользователям, с правом 'read'.
     * @param number Номер телефона. null - не используется в фильтре.
     * @param type   Тип телефона (PhoneType). null - не используется в фильтре.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-all-with-filter")
    public ResponseEntity<List<PhoneDTO>> getAllWithFilter(@RequestParam(required = false) String number, @RequestParam(required = false) String type) {
        return ResponseEntity.ok(phoneService.getPhones(number, type));
    }

    /**
     * Возвращает список телефонов с подходящим номером.
     * @param number Номер телефона, или его часть.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/search")
    public ResponseEntity<List<PhoneDTO>> searchByNumber(@RequestParam String number) {
        return ResponseEntity.ok(phoneService.searchPhones(number));
    }


    /**
     * Возвращает список всех телефонов.
     * Доступна всем авторизированным пользователям, с правом 'read'.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-all")
    public ResponseEntity<List<PhoneDTO>> getAll() {
        return ResponseEntity.ok(phoneService.getAllPhones());
    }


    /**
     * Добавление телефона.
     * Только для пользоватаелей с правом 'write'
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/create")
    public ResponseEntity<PhoneDTO> create(@RequestBody PhoneDTO phoneDTO) {
        return ResponseEntity.ok(phoneService.create(phoneDTO));
    }

    /**
     * Обновление данных о телефоне.
     * Только для админов.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    public ResponseEntity<PhoneDTO> update(@RequestBody PhoneDTO phoneDTO) {
        return ResponseEntity.ok(phoneService.update(phoneDTO));
    }

    /**
     * Удаление телефона.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete")
    public ResponseEntity<PhoneDTO> delete(@RequestParam long id) {
        phoneService.delete(id);
        return ResponseEntity.ok().build();
    }


}
