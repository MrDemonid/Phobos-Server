package mr.demonid.storage.service.makers;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.domain.Phone;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PhoneMaker {

    private ObjectEntityRepository objectEntityRepository;
    private PersonRepository personRepository;

    public PhoneDTO phoneToDto(Phone p) {
        if (p == null) {
            return null;
        }
        return new PhoneDTO(
                p.getId(), p.getNumber(), p.getType(), p.getDescription(),
                p.getObject() == null ? null : p.getObject().getId(),
                p.getPerson() == null ? null : p.getPerson().getTabNo()
        );
    }

    public Phone dtoToPhone(PhoneDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Phone(
                dto.getId(),
                dto.getNumber(),
                dto.getType(),
                dto.getDescription(),
                dto.getObjectId() == null ? null : objectEntityRepository.findById(dto.getObjectId()).orElse(null),
                dto.getPersonTabNo() == null ? null : personRepository.findPersonDetails(dto.getPersonTabNo()).orElse(null)
        );
    }

}
