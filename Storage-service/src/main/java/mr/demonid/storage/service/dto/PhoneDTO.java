package mr.demonid.storage.service.dto;

import lombok.*;
import mr.demonid.storage.service.domain.PhoneType;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {
    private Long id;
    private String number;
    private PhoneType type;
    private String description;

    private Long objectId;
    private Long personTabNo;

    /*
    Обеспечиваем уникальность для каждого объекта.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PhoneDTO phoneDTO = (PhoneDTO) o;
        return id.equals(phoneDTO.id) && number.equals(phoneDTO.number) && type == phoneDTO.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
