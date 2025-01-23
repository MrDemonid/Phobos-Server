package mr.demonid.storage.service.makers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.domain.ObjectEntity;
import mr.demonid.storage.service.domain.Person;
import mr.demonid.storage.service.domain.WorkSchedule;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class WorkScheduleMaker {

    private ObjectEntityRepository objectEntityRepository;
    private PersonRepository personRepository;

    /*
        Конвертация из сущности в DTO
     */
    public WorkScheduleDTO toDTO(WorkSchedule ws) {
        return new WorkScheduleDTO(
                ws.getId(), ws.getScheduleDetails(),
                ws.getObjects().stream().map(ObjectEntity::getId).collect(Collectors.toSet()),
                ws.getPersons().stream().map(Person::getTabNo).collect(Collectors.toSet())
        );
    }

    /*
        Конвертация из DTO в сущность.
     */
    public WorkSchedule toSchedule(WorkScheduleDTO dto) {
        return new WorkSchedule(
                dto.getId(),
                dto.getScheduleDetails(),
                dto.getObjectsId().stream().map(o -> objectEntityRepository.findById(o).orElse(null)).collect(Collectors.toSet()),
                dto.getPersonsTabNo().stream().map(o -> personRepository.findById(o).orElse(null)).collect(Collectors.toSet())
        );
    }
}
