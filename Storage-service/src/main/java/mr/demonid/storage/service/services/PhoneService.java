package mr.demonid.storage.service.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.storage.service.domain.Phone;
import mr.demonid.storage.service.domain.PhoneType;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.exceptions.BadPhoneException;
import mr.demonid.storage.service.makers.PhoneMaker;
import mr.demonid.storage.service.repository.PhoneRepository;
import mr.demonid.storage.service.services.filters.PhoneFilter;
import mr.demonid.storage.service.services.filters.PhoneSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class PhoneService {

    private PhoneMaker phoneMaker;
    private PhoneRepository phoneRepository;


    /**
     * Возвращает телефон по его id.
     */
    public PhoneDTO getById(Long id) {
        try {
            Optional<Phone> phone = phoneRepository.findByIdWithDependencies(id);
            if (phone.isPresent()) {
                Phone t = phone.get();
                return phoneMaker.phoneToDto(t);
            }
            throw new BadPhoneException("Телефон не найден");

        } catch (Exception e) {
            log.error("getById(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }


    /**
     * Возвращает список телефонов, выбранных по заданным параметрам.
     * @param number Номер телефона. null - не используется в фильтре.
     * @param type   Тип телефона (PhoneType). null - не используется в фильтре.
     */
    public List<PhoneDTO> getPhones(String number, String type) {
        if (number == null && type == null) {
            return getAllPhones();
        }
        try {
            PhoneFilter phoneFilter = new PhoneFilter(number, type == null ? null : PhoneType.valueOf(type.toUpperCase()));

            // TODO: заменить на полную загрузку выбранных
            List<Phone> items = phoneRepository.findAll(PhoneSpecification.filterBy(phoneFilter));
            return items.stream().map(phoneMaker::phoneToDto).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("getPhones(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }

    /**
     * Возвращает список телефонов с подходящими номерами.
     * @param number Номер телефона или его часть.
     */
    public List<PhoneDTO> searchPhones(String number) {
        if (number == null || number.isEmpty()) {
            throw new BadPhoneException("Недопустимый номер для поиска");
        }
        try {
            List<Phone> phones = phoneRepository.findAllWithLimit10(PageRequest.of(0, 10), "%" + number + "%");
            return phones.stream().map(phoneMaker::phoneToDto).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("searchPhones(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }

    /**
     * Возвращает список всех телефонов.
     */
    public List<PhoneDTO> getAllPhones() {
        try {
            List<Phone> items = phoneRepository.findAllIdWithDependencies();
            return items.stream().map(phoneMaker::phoneToDto).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("getAllPhones(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }

    /**
     * Добавление нового телефона, без зависимостей.
     */
    public PhoneDTO create(PhoneDTO phone) {
        try {
            if (phone.getId() != null) {
                throw new BadPhoneException("Такой телефон уже существует!");
            }
            phone.setObjectId(null);
            phone.setPersonTabNo(null);
            Phone res = phoneRepository.save(phoneMaker.dtoToPhone(phone));
            return phoneMaker.phoneToDto(res);

        } catch (Exception e) {
            log.error("create(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }

    /**
     * Обновление данных телефона.
     */
    public PhoneDTO update(PhoneDTO request) {
        try {
            Optional<Phone> phone = phoneRepository.findById(request.getId());
            if (phone.isEmpty()) {
                throw new BadPhoneException("Такого телефона не существует");
            }
            Phone t = phone.get();
            t.setNumber(request.getNumber());
            t.setType(request.getType());
            t.setDescription(request.getDescription());
            t = phoneRepository.save(t);
            return phoneMaker.phoneToDto(t);

        } catch (Exception e) {
            log.error("update(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }

    /**
     * Удаление телефона.
     */
    public void delete(Long id) {
        try {
            Optional<Phone> phone = phoneRepository.findById(id);
            if (phone.isEmpty()) {
                throw new BadPhoneException("Такого телефона не существует");
            }
            phoneRepository.deleteById(id);

        } catch (Exception e) {
            log.error("delete(): {}", e.getMessage());
            throw new BadPhoneException(e.getMessage());
        }
    }

}
