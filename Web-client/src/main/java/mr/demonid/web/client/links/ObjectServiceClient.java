package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.ObjectEntityDTO;
import mr.demonid.web.client.dto.PageDTO;
import mr.demonid.web.client.service.filters.ObjectFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@FeignClient(name = "gateway-objects", url = "http://localhost:9010", configuration = FeignClientConfig.class)

@FeignClient(name = "STORAGE-SERVICE", contextId = "objectServiceClient", configuration = FeignClientConfig.class)
public interface ObjectServiceClient {

    @PostMapping("/api/storage/objects/get-short-list/filter")
    ResponseEntity<PageDTO<ObjectEntityDTO>> getAllPageableWithFilter(@RequestBody ObjectFilter filter, Pageable pageable);

    @PostMapping("/api/storage/objects/create")
    ResponseEntity<ObjectEntityDTO> create(@RequestBody ObjectEntityDTO objectEntityDTO);

    @PutMapping("/api/storage/objects/update")
    ResponseEntity<ObjectEntityDTO> update(@RequestBody ObjectEntityDTO objectEntityDTO);

    @DeleteMapping("/api/storage/objects/delete/{id}")
    ResponseEntity<Void> delete(@PathVariable("id") Long id);


    @GetMapping("/api/storage/objects/get-by-id/{objectId}")
    ResponseEntity<ObjectEntityDTO> getById(@PathVariable Long objectId);

    @PostMapping("/api/storage/objects/link/schedule/{objectId}/{scheduleId}")
    ResponseEntity<ObjectEntityDTO> linkSchedule(@PathVariable Long objectId, @PathVariable Long scheduleId);

    @PostMapping("/api/storage/objects/unlink/schedule/{objectId}/{scheduleId}")
    ResponseEntity<ObjectEntityDTO> unlinkSchedule(@PathVariable Long objectId, @PathVariable Long scheduleId);

    @PostMapping("/api/storage/objects/link/phone/{objectId}/{phoneId}")
    ResponseEntity<ObjectEntityDTO> linkPhone(@PathVariable Long objectId, @PathVariable Long phoneId);

    @PostMapping("/api/storage/objects/unlink/phone/{objectId}/{phoneId}")
    ResponseEntity<ObjectEntityDTO> unlinkPhone(@PathVariable Long objectId, @PathVariable Long phoneId);

    @PostMapping("/api/storage/objects/link/person/{objectId}/{tabNo}")
    ResponseEntity<ObjectEntityDTO> linkPerson(@PathVariable Long objectId, @PathVariable Long tabNo);

    @PostMapping("/api/storage/objects/unlink/person/{objectId}/{tabNo}")
    ResponseEntity<ObjectEntityDTO> unlinkPerson(@PathVariable Long objectId, @PathVariable Long tabNo);

}
