package mr.demonid.storage.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectEntityDTO {
    private Long id;                // абсолютный номер объекта (ретранслятор*120)
    private String description;
    private String address;
    private List<String> photos = new ArrayList<>();
    private Set<PersonDTO> persons = new HashSet<>();
    private Set<PhoneDTO> phones = new HashSet<>();
    private Set<WorkScheduleDTO> workSchedules = new HashSet<>();
}

