package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.AddressDTO;
import mr.demonid.web.client.dto.PersonDTO;
import mr.demonid.web.client.dto.PhoneDTO;
import mr.demonid.web.client.service.PersonService;
import mr.demonid.web.client.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/web-service/person")
public class PersonRestController {


    private final PersonService personService;
    private final PhoneService phoneService;


    /**
     * Добавление нового пользователя.
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/new")
    ResponseEntity<PersonDTO> newPerson(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.ok(personService.createPerson(personDTO));
    }

    /**
     * Изменение данных существующего пользователя.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    ResponseEntity<Void> updatePerson(@RequestBody PersonDTO personDTO) {
        PersonDTO peson = personService.updatePerson(personDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление пользователя.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete/{tabNo}")
    ResponseEntity<Void> deletePerson(@PathVariable Long tabNo) {
        personService.deletePerson(tabNo);
        return ResponseEntity.ok().build();
    }



    /*=======================================================================
        API для привязки/удаления связей сотруднику
     =======================================================================*/

    /**
     * Поиск сотрудников по фамилии.
     * @param query Часть фамилии.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PersonDTO>> searchPersons(@RequestParam String query) {
        List<PersonDTO> persons = personService.searchPersonsByLastName(query);
        return ResponseEntity.ok(persons);
    }


    /**
     * Привязка телефона к сотруднику.
     * @param personId Идентификатор сотрудника.
     * @param phoneId  Идентификатор телефона.
     */
    @PostMapping("/{personId}/link-phone/{phoneId}")
    public ResponseEntity<?> addPhoneToPerson(@PathVariable Long personId, @PathVariable Long  phoneId) {
        return returnJsonResponse(personService.linkPhone(personId, phoneId));
    }

    /**
     * Отвязка телефона от сотрудника.
     * @param tabNo    Идентификатор сотрудника.
     * @param phoneId  Идентификатор телефона.
     */
    @PostMapping("/{tabNo}/unlink-phone/{phoneId}")
    public ResponseEntity<?> unlinkPhoneToPerson(@PathVariable Long tabNo, @PathVariable Long  phoneId) {
        return returnJsonResponse(personService.unlinkPhone(tabNo, phoneId));
    }


    /**
     * Добавление адреса сотрудника.
     * @param tabNo   Идентификатор сотрудника.
     * @param address Адрес.
     */
    @PostMapping("/add/address/{tabNo}")
    public ResponseEntity<?> addAddress(@PathVariable Long tabNo, @RequestBody AddressDTO address) {
        return returnJsonResponse(personService.addAddress(tabNo, address));
    }

    /**
     * Удаление адреса сотрудника.
     * @param tabNo   Идентификатор сотрудника.
     * @param address Адрес.
     */
    @PostMapping("/remove/address/{tabNo}")
    public ResponseEntity<?> removeAddress(@PathVariable Long tabNo, @RequestBody AddressDTO address) {
        return returnJsonResponse(personService.delAddress(tabNo, address));
    }


    /**
     * Привязывает сотруднику график работы.
     * @param tabNo      Идентификатор сотрудника.
     * @param scheduleId График работы.
     */
    @PostMapping("/{tabNo}/link/schedule/{scheduleId}")
    public ResponseEntity<?> linkSchedule(@PathVariable Long tabNo, @PathVariable Long scheduleId) {
        return returnJsonResponse(personService.addSchedule(tabNo, scheduleId));
    }

    /**
     * Удалет у сотрудника график работы.
     * @param tabNo      Идентификатор сотрудника.
     * @param scheduleId График работы.
     */
    @PostMapping("/{tabNo}/unlink/schedule/{scheduleId}")
    public ResponseEntity<?> unlinkSchedule(@PathVariable Long tabNo, @PathVariable Long scheduleId) {
        return returnJsonResponse(personService.delSchedule(tabNo, scheduleId));
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
