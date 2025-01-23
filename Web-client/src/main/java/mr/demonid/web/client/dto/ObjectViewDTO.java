package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для отображения данных объектов в виде таблиц.
 * Сделан ради компактного представления телефонов и ответственных лиц.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectViewDTO {
    private Long id;                // абсолютный номер объекта (ретранслятор*120)

    private String description;
    private String address;

    private List<String> photos = new ArrayList<>();

    private String personsList;
    private String phonesList;


    public static ObjectViewDTO toView(ObjectEntityDTO src) {
        StringBuilder persons = new StringBuilder();
        StringBuilder phones = new StringBuilder();

        src.getPersons().forEach(p -> {
            persons.append(p.getLastName()).append(" ").append(p.getFirstName().charAt(0)).append(".").append(p.getMiddleName().charAt(0)).append("., ");
        });
        if (!persons.isEmpty()) {
            persons.delete(persons.length() - 2, persons.length());
        }
        src.getPhones().forEach(t -> {
           phones.append(t.getNumber()).append(", ");
        });
        if (!phones.isEmpty()) {
            phones.delete(phones.length() - 2, phones.length());
        }
        return new ObjectViewDTO(src.getId(), src.getDescription(), src.getAddress(), src.getPhotos(), persons.toString(), phones.toString());
    }
}
