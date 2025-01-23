package mr.demonid.storage.service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectEntity {

    @Id
    private Long id;                // абсолютный номер объекта (ретранслятор*120)

    private String description;
    private String address;

    @ElementCollection
    private List<String> photos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "object_person", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> persons = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "object")
    private Set<Phone> phones = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "object_work_schedule", joinColumns = @JoinColumn(name = "object_id"), inverseJoinColumns = @JoinColumn(name = "work_schedule_id"))
    private Set<WorkSchedule> workSchedules = new HashSet<>();

}
