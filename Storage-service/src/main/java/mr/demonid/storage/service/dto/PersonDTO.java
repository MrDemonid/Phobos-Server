package mr.demonid.storage.service.dto;

import lombok.*;
import mr.demonid.storage.service.domain.GenderType;

import java.time.LocalDate;
import java.util.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class PersonDTO{
    private Long tabNo;                                 // уникальный табельный номер сотрудника
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private GenderType gender;
    private Set<String> photos = new HashSet<>();
    private Set<Long> objects = new HashSet<>();        // id связанных с сотрудником объектов
    private Set<PhoneDTO> phones =  new HashSet<>();
    private Set<WorkScheduleDTO> workSchedules =  new HashSet<>();
    private Set<AddressDTO> addresses =  new HashSet<>();

    /*
    Обеспечиваем уникальность для каждого объекта.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PersonDTO personDTO = (PersonDTO) o;
        return tabNo.equals(personDTO.tabNo) && firstName.equals(personDTO.firstName) && lastName.equals(personDTO.lastName) && Objects.equals(middleName, personDTO.middleName) && Objects.equals(birthDate, personDTO.birthDate) && gender == personDTO.gender;
    }

    @Override
    public int hashCode() {
        int result = tabNo.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + Objects.hashCode(middleName);
        result = 31 * result + Objects.hashCode(birthDate);
        result = 31 * result + gender.hashCode();
        return result;
    }
}
