package mr.demonid.web.client.links;

import mr.demonid.web.client.configs.FeignClientConfig;
import mr.demonid.web.client.dto.AddressDTO;
import mr.demonid.web.client.dto.PageDTO;
import mr.demonid.web.client.dto.PersonDTO;
import mr.demonid.web.client.service.filters.PersonFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "gateway-person", url = "http://localhost:9010", configuration = FeignClientConfig.class)

@FeignClient(name = "STORAGE-SERVICE", contextId = "personServiceClient", configuration = FeignClientConfig.class)
public interface PersonServiceClient {

    @PostMapping("/api/storage/persons/get-short-list/filter")
    ResponseEntity<PageDTO<PersonDTO>> getAllPageableWithFilter(@RequestBody PersonFilter filter, Pageable pageable);

    @GetMapping("/api/storage/persons/get-by-tabNo/{tabNo}")
    ResponseEntity<PersonDTO> getByTabNo(@PathVariable Long tabNo);

    @PostMapping("/api/storage/persons/create")
    ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO);

    @PutMapping("/api/storage/persons/update")
    ResponseEntity<PersonDTO> update(@RequestBody PersonDTO personDTO);

    @GetMapping("/api/storage/persons/search")
    ResponseEntity<List<PersonDTO>> search(@RequestParam String lastName);

    @DeleteMapping("/api/storage/persons/delete/{tabNo}")
    ResponseEntity<Void> delete(@PathVariable("tabNo") Long tabNo);

    @PostMapping("/api/storage/persons/link/phone/{tabNo}/{phoneId}")
    ResponseEntity<PersonDTO> linkPhone(@PathVariable Long tabNo, @PathVariable Long phoneId);

    @PostMapping("/api/storage/persons/unlink/phone/{tabNo}/{phoneId}")
    ResponseEntity<PersonDTO> unlinkPhone(@PathVariable Long tabNo, @PathVariable Long phoneId);

    @PostMapping("/api/storage/persons/link/address/{tabNo}")
    ResponseEntity<PersonDTO> linkAddress(@PathVariable Long tabNo, @RequestBody AddressDTO addressDTO);

    @PostMapping("/api/storage/persons/unlink/address/{tabNo}")
    ResponseEntity<Void> unlinkAddress(@PathVariable Long tabNo, @RequestBody AddressDTO addressDTO);

    @PostMapping("/api/storage/persons/link/schedule/{tabNo}/{scheduleId}")
    ResponseEntity<PersonDTO> linkSchedule(@PathVariable Long tabNo, @PathVariable Long scheduleId);

    @PostMapping("/api/storage/persons/unlink/schedule/{tabNo}/{scheduleId}")
    ResponseEntity<PersonDTO> unlinkSchedule(@PathVariable Long tabNo, @PathVariable Long scheduleId);

}
