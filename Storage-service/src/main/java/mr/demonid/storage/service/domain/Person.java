package mr.demonid.storage.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Класс сотрудника.
 * В целях недопущения лишних затрат при обращении к БД, все
 * связанные поля объявлены как LAZY, поэтому для их загрузки
 * нужно использовать явное связывание (JOIN FETCH).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    private Long tabNo;

    @Column(length = 32)
    private String firstName;

    @Column(length = 32, nullable = false)
    private String lastName;

    @Column(length = 32)
    private String middleName;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<String> photos = new HashSet<>();

    @ManyToMany(mappedBy = "persons")
    private Set<ObjectEntity> objects = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "person", fetch = FetchType.LAZY)
    private Set<Phone> phones = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person_work_schedule", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "work_schedule_id"))
    private Set<WorkSchedule> workSchedules = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    /*
        Чтобы избежать лишних затрат ресурсов, выборку связанных объектов
        буду проводить либо через БД объектов, либо SQL-запросом напрямую
        с БД object_person. В последнем случае получаем максимальную
        скорость и экономию памяти, но теряем в переносимости и поддержке кода.
        Так что оставлю этот вариант как резервный (будет закомментирован в репозитории).

        Соответственно это поле связи нам больше не нужно.
    @ManyToMany(mappedBy = "persons", fetch = FetchType.LAZY)
    private Set<ObjectEntity> objects = new HashSet<>();
     */

    /*
    Обеспечиваем уникальность для каждого объекта.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return tabNo.equals(person.tabNo) && firstName.equals(person.firstName) && lastName.equals(person.lastName) && Objects.equals(middleName, person.middleName) && Objects.equals(birthDate, person.birthDate) && gender.equals(person.gender);
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

    @Override
    public String toString() {
        return "Person{" +
                "tabNo=" + tabNo +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                ", photos=" + photos +
                ", phones=" + (phones == null ? "[]" : phones.stream().map(Phone::getNumber).collect(Collectors.toSet())) +
                ", workSchedules=" + (workSchedules == null ? "[]" : workSchedules.stream().map(WorkSchedule::getScheduleDetails).collect(Collectors.toSet())) +
                ", addresses=" + addresses +
                '}';
    }
}
