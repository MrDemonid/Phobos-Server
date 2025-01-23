package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.PhoneDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@FeignClient(name = "gateway-phones", url = "http://localhost:9010", configuration = FeignClientConfig.class)

@FeignClient(name = "STORAGE-SERVICE", contextId = "phoneServiceClient", configuration = FeignClientConfig.class)
public interface PhoneServiceClient {

    /**
     * Возвращает телефон по его id.
     */
    @GetMapping("/api/storage/phones/get-by_id")
    ResponseEntity<PhoneDTO> getById(@RequestParam long id);

    /**
     * Возвращает список телефонов, выбранных по заданным параметрам.
     * @param number Номер телефона. null - не используется в фильтре.
     * @param type   Тип телефона (PhoneType). null - не используется в фильтре.
     */
    @GetMapping("/api/storage/phones/get-all-with-filter")
    ResponseEntity<List<PhoneDTO>> getAllWithFilter(@RequestParam(required = false) String number, @RequestParam(required = false) String type);

    /**
     * Возвращает список телефонов с подходящим номером.
     * @param number Номер телефона, или его часть.
     */
    @GetMapping("/api/storage/phones/search")
    ResponseEntity<List<PhoneDTO>> searchByNumber(@RequestParam String number);

    /**
     * Возвращает список всех телефонов.
     */
    @GetMapping("/api/storage/phones/get-all")
    ResponseEntity<List<PhoneDTO>> getAll();

    /**
     * Добавление телефона.
     */
    @PostMapping("/api/storage/phones//create")
    ResponseEntity<PhoneDTO> create(@RequestBody PhoneDTO phoneDTO);

    /**
     * Обновление данных о телефоне.
     */
    @PutMapping("/api/storage/phones/update")
    ResponseEntity<PhoneDTO> update(@RequestBody PhoneDTO phoneDTO);

    /**
     * Удаление телефона.
     */
    @DeleteMapping("/api/storage/phones/delete")
    ResponseEntity<PhoneDTO> delete(@RequestParam long id);

}
