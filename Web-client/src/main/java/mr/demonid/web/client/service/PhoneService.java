package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.PhoneDTO;
import mr.demonid.web.client.dto.PhoneType;
import mr.demonid.web.client.exceptions.PhoneException;
import mr.demonid.web.client.exceptions.WorkScheduleException;
import mr.demonid.web.client.links.PhoneServiceClient;
import mr.demonid.web.client.service.filters.PhoneFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class PhoneService {

    private PhoneServiceClient phoneServiceClient;


    /**
     * Возвращает телефон по его ID.
     */
    public PhoneDTO getPhoneById(Long id) {
        try {
            return phoneServiceClient.getById(id).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PhoneException(error);
        }
    }

    public List<PhoneDTO> getAllPhones() {
        try {
            return phoneServiceClient.getAll().getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return new ArrayList<>();
        }
    }

    /**
     * Возвращает список выбранных по фильтру телефонов.
     * @param number null - если не используется.
     * @param type   null - если не используется.
     */
    public List<PhoneDTO> getPhonesWithFilter(String number, String type) {
        try {
            return phoneServiceClient.getAllWithFilter(number, type).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
        return new ArrayList<>();
    }

    public List<PhoneDTO> searchPhonesByNumber(String number) {
        try {
            return phoneServiceClient.searchByNumber(number).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return new ArrayList<>();
        }
    }

    /**
     * Добавление в БД нового телефона.
     */
    public PhoneDTO createPhone(PhoneDTO phoneDTO) {
        try {
            return phoneServiceClient.create(phoneDTO).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PhoneException(error);
        }
    }

    /**
     * Обновление данных существующего телефона.
     */
    public PhoneDTO updatePhone(PhoneDTO phoneDTO) {
        try {
            return phoneServiceClient.update(phoneDTO).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PhoneException(error);
        }
    }

    /**
     * Удаление телефона.
     */
    public void deletePhone(Long id) {
        try {
            phoneServiceClient.delete(id);
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new PhoneException(error);
        }
    }

}
