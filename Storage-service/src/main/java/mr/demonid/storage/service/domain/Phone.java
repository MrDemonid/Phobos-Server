package mr.demonid.storage.service.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PhoneType type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "object_id")
    private ObjectEntity object;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    /*
    Обеспечиваем уникальность для каждого объекта.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Phone phone = (Phone) o;
        return id.equals(phone.id) && number.equals(phone.number) && type == phone.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", object=" + (object == null ? "[]" : object.getId()) +
                ", person=" + (person == null ? "[]" : person.getTabNo()) +
                '}';
    }
}
