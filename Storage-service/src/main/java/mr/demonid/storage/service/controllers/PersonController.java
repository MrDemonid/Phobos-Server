package mr.demonid.storage.service.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.dto.AddressDTO;
import mr.demonid.storage.service.dto.PageDTO;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.services.PersonService;
import mr.demonid.storage.service.services.filters.PersonFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Контроллер для базы данных сотрудников.
 *
 * Поскольку у нас права каждому пользователю назначает
 * непосредственно админ, то для каждого эндпоинта
 * указаны условия доступа, независимо от его роли.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/storage/persons")
public class PersonController {

    private PersonService personService;


    /**
     * Добавление сотрудника.
     * Только для пользователей с правом 'write'
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/create")
    public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.create(personDTO));
    }

    /**
     * Обновление данных о сотруднике.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.update(personDTO));
    }

    /**
     * Удаление пользователя.
     * @param tabNo Табельный номер пользователя.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete/{tabNo}")
    public ResponseEntity<Void> delete(@PathVariable("tabNo") Long tabNo) {
        personService.delete(tabNo);
        return ResponseEntity.noContent().build();
    }

    /**
     * Постраничная выборка базовых данных о пользователях.
     * @param pageable Страница, с которой выбираем данные.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @PostMapping("/get-short-list/filter")
    public ResponseEntity<PageDTO<PersonDTO>> getAllPageableWithFilter(@RequestBody PersonFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(personService.getAllPageableWithFilter(filter, pageable)));
    }


    /**
     * Получение подробных данных о пользователе.
     * @param tabNo Табельный номер.
     */
    @PreAuthorize("hasAuthority('SCOPE_read')")
    @GetMapping("/get-by-tabNo/{tabNo}")
    public ResponseEntity<PersonDTO> getByTabNo(@PathVariable Long tabNo) {
        return ResponseEntity.ok(personService.getPersonByTabNo(tabNo));
    }


    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_read')")
    public ResponseEntity<List<PersonDTO>> search(@RequestParam String lastName) {
        System.out.println("-- search: " + lastName);
        return ResponseEntity.ok(personService.searchPersons(lastName));
    }

    /**
     * Привязка телефона к сотруднику.
     * @param tabNo   Табельный номер сотрудника.
     * @param phoneId Идентификатор телефона.
     */
    @PostMapping("/link/phone/{tabNo}/{phoneId}")
    public ResponseEntity<PersonDTO> linkPhone(@PathVariable Long tabNo, @PathVariable Long phoneId) {
        return ResponseEntity.ok(personService.linkPhone(tabNo, phoneId));
    }

    /**
     * Отвязка телефона от сотрудника.
     * @param tabNo   Табельный номер сотрудника.
     * @param phoneId Идентификатор телефона.
     */
    @PostMapping("/unlink/phone/{tabNo}/{phoneId}")
    public ResponseEntity<PersonDTO> unlinkPhone(@PathVariable Long tabNo, @PathVariable Long phoneId) {
        return ResponseEntity.ok(personService.unlinkPhone(tabNo, phoneId));
    }


    /**
     * Добавление адреса сотруднику.
     * @param tabNo      Табельный номер сотрудника.
     * @param addressDTO Адрес.
     */
    @PostMapping("/link/address/{tabNo}")
    public ResponseEntity<PersonDTO> linkAddress(@PathVariable Long tabNo, @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(personService.addAddress(tabNo, addressDTO));
    }

    /**
     * Удаления адреса сотрудника.
     * @param tabNo      Табельный номер сотрудника.
     * @param addressDTO Адрес.
     */
    @PostMapping("/unlink/address/{tabNo}")
    public ResponseEntity<PersonDTO> unlinkAddress(@PathVariable Long tabNo, @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(personService.delAddress(tabNo, addressDTO));
    }


    /**
     * Привязка расписания работы пользователю.
     * @param tabNo      Табельный номер сотрудника.
     * @param scheduleId Идентификатор расписания.
     */
    @PostMapping("/link/schedule/{tabNo}/{scheduleId}")
    public ResponseEntity<PersonDTO> linkSchedule(@PathVariable Long tabNo, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(personService.addSchedule(tabNo, scheduleId));
    }

    /**
     * Отвязка расписания работы от пользователя.
     * @param tabNo      Табельный номер сотрудника.
     * @param scheduleId Идентификатор расписания.
     */
    @PostMapping("/unlink/schedule/{tabNo}/{scheduleId}")
    public ResponseEntity<PersonDTO> unlinkSchedule(@PathVariable Long tabNo, @PathVariable Long scheduleId) {
        return ResponseEntity.ok(personService.delSchedule(tabNo, scheduleId));
    }
}
