package mr.demonid.storage.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class WorkScheduleDTO {
    private Long id;
    private String scheduleDetails;

    private Set<Long> objectsId = new HashSet<>();
    private Set<Long> personsTabNo = new HashSet<>();


    public WorkScheduleDTO(Long id, String scheduleDetails, Set<Long> objectsId, Set<Long> personsTabNo) {
        this.id = id;
        this.scheduleDetails = scheduleDetails;
        this.objectsId = objectsId == null ? new HashSet<>() : objectsId;
        this.personsTabNo = personsTabNo == null ? new HashSet<>() : personsTabNo;
    }

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
