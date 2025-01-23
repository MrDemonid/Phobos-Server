package mr.demonid.web.client.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Log4j2
public class PersonDTO{
    private Long tabNo;                         // уникальный табельный номер сотрудника
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


    public String formatDateTime() {
        try {
            return birthDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        } catch (Exception e) {
            log.warn("Date format exception: {}", e.getMessage());
            return "";
        }
    }

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
