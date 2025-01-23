package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.AddressDTO;
import mr.demonid.web.client.dto.PageDTO;
import mr.demonid.web.client.dto.PersonDTO;
import mr.demonid.web.client.dto.PhoneDTO;
import mr.demonid.web.client.exceptions.PersonException;
import mr.demonid.web.client.links.PersonServiceClient;
import mr.demonid.web.client.service.filters.PersonFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
@Log4j2
public class PersonService {

    private PersonServiceClient personServiceClient;
    private PhoneService phoneService;


    public PersonDTO getPersonById(Long id) {
        try {
            return personServiceClient.getByTabNo(id).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return new PersonDTO();
        }
    }

    public PageDTO<PersonDTO> getAllPersonsWithFilter(PersonFilter filter, Pageable pageable) {
        try {
            return personServiceClient.getAllPageableWithFilter(filter, pageable).getBody();

        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

    public PersonDTO createPerson(PersonDTO personDTO) {
        try {
            return personServiceClient.create(personDTO).getBody();

        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PersonException(error);
        }
    }

    public PersonDTO updatePerson(PersonDTO personDTO) {
        try {
            return personServiceClient.update(personDTO).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PersonException(error);
        }
    }

    public void deletePerson(Long tabNo) {
        try {
            personServiceClient.delete(tabNo);
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PersonException(error);
        }
    }

    /**
     * Возвращает список сотрудников, чья фамилия подходит под шаблон lastName.
     * @param lastName Полная фамилия, или её часть.
     */
    public List<PersonDTO> searchPersonsByLastName(String lastName) {
        try {
            System.out.println("-- search: " + lastName);
            return personServiceClient.search(lastName).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return new ArrayList<>();
        }
    }


    /**
     * Привязка телефона к пользователю.
     * @param tabNo   Табельный номер пользователя.
     * @param phoneId Идентификатор телефона.
     */
    public String linkPhone(Long tabNo, Long phoneId) {
        try {
            personServiceClient.linkPhone(tabNo, phoneId);
            return "";
        } catch (FeignException e) {
            log.error("linkPhone(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    /**
     * Отвязка телефона от пользователя.
     */
    public String unlinkPhone(Long tabNo, Long phoneId) {
        try {
            personServiceClient.unlinkPhone(tabNo, phoneId);
            return "";
        } catch (FeignException e) {
            log.error("unlinkPhone(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    /**
     * Сервис привязки адреса сотруднику.
     */
    public String addAddress(Long tabNo, AddressDTO addressDTO) {
        try {
            personServiceClient.linkAddress(tabNo, addressDTO);
            return "";
        } catch (FeignException e) {
            log.error("addAddress(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    /**
     * Сервис удаления адреса пользователя.
     */
    public String delAddress(Long tabNo, AddressDTO addressDTO) {
        try {
            personServiceClient.unlinkAddress(tabNo, addressDTO);
            return "";
        } catch (FeignException e) {
            log.error("delAddress(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }


    public String addSchedule(Long tabNo, Long scheduleId) {
        try {
            personServiceClient.linkSchedule(tabNo, scheduleId);
            return "";
        } catch (FeignException e) {
            log.error("addSchedule(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }
    public String delSchedule(Long tabNo, Long scheduleId) {
        try {
            personServiceClient.unlinkSchedule(tabNo, scheduleId);
            return "";
        } catch (FeignException e) {
            log.error("delSchedule(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }


}
