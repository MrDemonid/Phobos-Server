package mr.demonid.storage.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

/**
 * График работы.
 * Используется как для людей, так и для цехов.
 * Уникальность экземпляров определяется по полям: id и scheduleDetails
 */
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scheduleDetails;

    @ManyToMany(mappedBy = "workSchedules")
    private Set<ObjectEntity> objects = new HashSet<>();

    @ManyToMany(mappedBy = "workSchedules")
    private Set<Person> persons = new HashSet<>();

    /*
        Обеспечиваем уникальность для каждого объекта.
     */

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        WorkSchedule that = (WorkSchedule) o;
        return id.equals(that.id) && Objects.equals(scheduleDetails, that.scheduleDetails);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + Objects.hashCode(scheduleDetails);
        return result;
    }
}
