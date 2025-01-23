package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.WorkScheduleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@FeignClient(name = "gateway-work-schedule", url = "http://localhost:9010", configuration = FeignClientConfig.class)

@FeignClient(name = "STORAGE-SERVICE", contextId = "workScheduleClient", configuration = FeignClientConfig.class)
public interface WorkScheduleClient {

    /**
     * Возвращает режим по его id
     */
    @GetMapping("/api/storage/schedule/get-by-id")
    ResponseEntity<WorkScheduleDTO> getById(@RequestParam(value = "id") Long id);

    /**
     * Загрузка всех имеющихся в БД режимов.
     */
    @GetMapping("/api/storage/schedule/get-all")
    ResponseEntity<List<WorkScheduleDTO>> getAll();

    /**
     * Добавляет новый режим работы.
     */
    @PostMapping("/api/storage/schedule")
    ResponseEntity<WorkScheduleDTO> create(@RequestBody WorkScheduleDTO request);

    /**
     * Обновление существующего режима.
     */
    @PutMapping("/api/storage/schedule")
    ResponseEntity<WorkScheduleDTO> update(@RequestBody WorkScheduleDTO request);

    /**
     * Удаление режима.
     */
    @DeleteMapping("/api/storage/schedule")
    ResponseEntity<Void> delete(@RequestParam(value = "id") Long id);

}
