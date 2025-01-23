package mr.demonid.web.client.dto;

import lombok.*;

import java.util.Objects;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WorkScheduleDTO {
    private Long id;
    private String scheduleDetails;

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
