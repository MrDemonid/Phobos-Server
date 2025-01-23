package mr.demonid.storage.service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Column(length = 64)
    private String city;

    private String street;

    @Column(length = 50)
    private String postalCode;

    private String description;

    /*
    Обеспечиваем уникальность для каждого объекта.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(normalize(city), normalize(address.city)) &&
                Objects.equals(normalize(street), normalize(address.street)) &&
                Objects.equals(normalize(postalCode), normalize(address.postalCode)) &&
                Objects.equals(normalize(description), normalize(address.description));
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(normalize(city));
        result = 31 * result + Objects.hashCode(normalize(street));
        result = 31 * result + Objects.hashCode(normalize(postalCode));
        result = 31 * result + Objects.hashCode(normalize(description));
        return result;
    }

    // "Нормализация" строк, чтобы пробел и пустая строка считались равными
    // Нужна из-за того, что у объекта нет уникального Id.
    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
