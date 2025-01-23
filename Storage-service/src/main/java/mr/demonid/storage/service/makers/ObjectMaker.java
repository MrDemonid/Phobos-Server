package mr.demonid.storage.service.makers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.domain.ObjectEntity;
import mr.demonid.storage.service.dto.ObjectEntityDTO;
import mr.demonid.storage.service.services.PersonService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ObjectMaker {

    private PhoneMaker phoneMaker;
    private PersonMaker personMaker;
    private WorkScheduleMaker workScheduleMaker;
    private PersonService personService;


    public ObjectEntityDTO toDto(ObjectEntity src) {
        return new ObjectEntityDTO(
                src.getId(), src.getDescription(), src.getAddress(), src.getPhotos(),
                src.getPersons().stream().map(p -> personMaker.toDto(p, personService.loadPersonIdsByTabNo(p.getTabNo()))).collect(Collectors.toSet()),
                src.getPhones().stream().map(phoneMaker::phoneToDto).collect(Collectors.toSet()),
                src.getWorkSchedules().stream().map(workScheduleMaker::toDTO).collect(Collectors.toSet())
        );
    }

    public ObjectEntity toObject(ObjectEntityDTO dto) {
        return new ObjectEntity(
                dto.getId(), dto.getDescription(), dto.getAddress(), dto.getPhotos(),
                dto.getPersons().stream().map(personMaker::toPerson).collect(Collectors.toSet()),
                dto.getPhones().stream().map(phoneMaker::dtoToPhone).collect(Collectors.toSet()),
                dto.getWorkSchedules().stream().map(workScheduleMaker::toSchedule).collect(Collectors.toSet())
        );
    }

}
