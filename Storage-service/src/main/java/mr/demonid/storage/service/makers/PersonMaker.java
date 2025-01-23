package mr.demonid.storage.service.makers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.domain.*;
import mr.demonid.storage.service.dto.AddressDTO;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PersonMaker {

    private PhoneMaker phoneMaker;
    private WorkScheduleMaker workScheduleMaker;

    private ObjectEntityRepository objectEntityRepository;


    public Person toPerson(PersonDTO dto) {
        System.out.println("-- maker toPerson --: " + dto);
        return new Person(
                dto.getTabNo(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getMiddleName(),
                dto.getBirthDate(),
                dto.getGender(),
                dto.getPhotos(),
                dto.getObjects() == null ? null : dto.getObjects().stream().map(o -> objectEntityRepository.findById(o).orElse(null)).collect(Collectors.toSet()),
                dto.getPhones() == null ? null : dto.getPhones().stream().map(phoneMaker::dtoToPhone).collect(Collectors.toSet()),
                dto.getWorkSchedules() == null ? null : dto.getWorkSchedules().stream().map(workScheduleMaker::toSchedule).collect(Collectors.toSet()),
                dto.getAddresses() == null ? null : dto.getAddresses().stream().map(a -> new Address(a.getCity(), a.getStreet(), a.getPostalCode(), a.getDescription())).collect(Collectors.toSet())
        );
    }

    public PersonDTO toDto(Person person, Set<Long> objectIds) {
        return new PersonDTO(
                person.getTabNo(),
                person.getFirstName(),
                person.getLastName(),
                person.getMiddleName(),
                person.getBirthDate(),
                person.getGender(),
                person.getPhotos(),
                objectIds,
                person.getPhones() == null ? null : person.getPhones().stream().map(phoneMaker::phoneToDto).collect(Collectors.toSet()),
                person.getWorkSchedules() == null ? null : person.getWorkSchedules().stream().map(workScheduleMaker::toDTO).collect(Collectors.toSet()),
                person.getAddresses() == null ? null : person.getAddresses().stream().map(a -> new AddressDTO(a.getStreet(), a.getCity(), a.getPostalCode(), a.getDescription())).collect(Collectors.toSet())
        );
    }


}
