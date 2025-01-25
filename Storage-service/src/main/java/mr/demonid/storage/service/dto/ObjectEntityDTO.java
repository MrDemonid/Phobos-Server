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
public class ObjectEntityDTO {
    private Long id;                // абсолютный номер объекта (ретранслятор*120)
    private String description;
    private String address;
    private List<String> photos = new ArrayList<>();
    private Set<PersonDTO> persons = new HashSet<>();
    private Set<PhoneDTO> phones = new HashSet<>();
    private Set<WorkScheduleDTO> workSchedules = new HashSet<>();

    public ObjectEntityDTO(Long id, String description, String address, List<String> photos, Set<PersonDTO> persons, Set<PhoneDTO> phones, Set<WorkScheduleDTO> workSchedules) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.photos = photos == null ? new ArrayList<>() : photos;
        this.persons = persons == null ? new HashSet<>() : persons;
        this.phones = phones == null ? new HashSet<>() : phones;
        this.workSchedules = workSchedules == null ? new HashSet<>() : workSchedules;
    }
}

