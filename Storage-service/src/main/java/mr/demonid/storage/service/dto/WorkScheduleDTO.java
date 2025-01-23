package mr.demonid.storage.service.dto;

import jakarta.persistence.ManyToMany;
import lombok.*;
import mr.demonid.storage.service.domain.ObjectEntity;
import mr.demonid.storage.service.domain.Person;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WorkScheduleDTO {
    private Long id;
    private String scheduleDetails;

    private Set<Long> objectsId = new HashSet<>();
    private Set<Long> personsTabNo = new HashSet<>();

    /*
    Обеспечиваем уникальность для каждого объекта.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        WorkScheduleDTO that = (WorkScheduleDTO) o;
        return id.equals(that.id) && Objects.equals(scheduleDetails, that.scheduleDetails);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + Objects.hashCode(scheduleDetails);
        return result;
    }
}
