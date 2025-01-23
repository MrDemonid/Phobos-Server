package mr.demonid.web.client.controllers;

import lombok.AllArgsConstructor;
import mr.demonid.web.client.dto.PhoneDTO;
import mr.demonid.web.client.dto.PhoneType;
import mr.demonid.web.client.dto.WorkScheduleDTO;
import mr.demonid.web.client.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@AllArgsConstructor
@RequestMapping("/api/web-service/phone")
public class PhoneRestController {

    private PhoneService phoneService;


    /**
     * Добавление нового телефона.
     */
    @PreAuthorize("hasAuthority('SCOPE_write')")
    @PostMapping("/new")
    public ResponseEntity<PhoneDTO> addNewSchedule(@RequestBody PhoneDTO phoneDTO) {
        System.out.println("add new phone: " + phoneDTO);
        PhoneDTO phone = phoneService.createPhone(phoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(phone);
    }

    /**
     * Изменение существующего телефона.
     */
    @PreAuthorize("hasAuthority('SCOPE_update')")
    @PutMapping("/update")
    public ResponseEntity<Void> updatePhone(@RequestBody PhoneDTO phoneDTO) {
        PhoneDTO phone = phoneService.updatePhone(phoneDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаление телефона по его id.
     */
    @PreAuthorize("hasAuthority('SCOPE_delete')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
        phoneService.deletePhone(id);
        return ResponseEntity.ok().build();
    }

    /**
     * API для Java Script: возвращает список типов телефонов.
     */
    @GetMapping("/types")
    public Map<String, String> getPhoneTypeDescriptions() {
        return Stream.of(PhoneType.values()).collect(Collectors.toMap(Enum::name, PhoneType::getDescription));
    }

    /**
     * Поиск телефонов по введенному пользователем номеру, или его части.
     * @param query Вводимый пользователем номер телефона.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PhoneDTO>> searchPhones(@RequestParam String query) {
        List<PhoneDTO> phones = phoneService.searchPhonesByNumber(query);
        return ResponseEntity.ok(phones);
    }

}
