package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.ObjectEntityDTO;
import mr.demonid.web.client.dto.PageDTO;
import mr.demonid.web.client.exceptions.ObjectException;
import mr.demonid.web.client.links.ObjectServiceClient;
import mr.demonid.web.client.service.filters.ObjectFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ObjectService {

    private ObjectServiceClient objectServiceClient;


    /**
     * Создание нового объекта.
     */
    public ObjectEntityDTO createObject(ObjectEntityDTO objectEntityDTO) {
        try {
            return objectServiceClient.create(objectEntityDTO).getBody();

        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error("ObjectEntity.createObject(): {}", error);
            throw new ObjectException(error);
        }
    }

    /**
     * Обновление данных об объекте.
     */
    public ObjectEntityDTO updateObject(ObjectEntityDTO objectEntityDTO) {
        try {
            return objectServiceClient.update(objectEntityDTO).getBody();

        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error("ObjectEntity.updateObject(): {}", error);
            throw new ObjectException(error);
        }
    }

    /**
     * Удаление объекта.
     */
    public void deleteObject(Long objectId) {
        try {
            objectServiceClient.delete(objectId);
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error("ObjectEntity.deleteObject(): {}", error);
            throw new ObjectException(error);
        }
    }


    /**
     * Возвращает список объектов, выбранный по заданному фильтру и странице.
     * @param filter   Фильтр для выборки нужных данных.
     * @param pageable Размер и номер выбираемой страницы.
     */
    public PageDTO<ObjectEntityDTO> getAllObjectsWithFilter(ObjectFilter filter, Pageable pageable) {
        try {
            return objectServiceClient.getAllPageableWithFilter(filter, pageable).getBody();

        } catch (FeignException e) {
            log.error("ObjectEntity.getAllObjects(): {}",e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }


    /*====================================================================================
        Методы для работы с Object Details (привязка/удаления связей)
     =====================================================================================*/
    public ObjectEntityDTO getObjectById(Long objectId) {
        try {
            return objectServiceClient.getById(objectId).getBody();
        } catch (FeignException e) {
            log.error("ObjectEntity.getObjectById(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return new ObjectEntityDTO();
        }
    }


    public String addSchedule(Long tabNo, Long scheduleId) {
        try {
            objectServiceClient.linkSchedule(tabNo, scheduleId);
            return "";
        } catch (FeignException e) {
            log.error("ObjectEntity.addSchedule(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }
    public String delSchedule(Long tabNo, Long scheduleId) {
        try {
            objectServiceClient.unlinkSchedule(tabNo, scheduleId);
            return "";
        } catch (FeignException e) {
            log.error("ObjectEntity.delSchedule(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }


    public String linkPhone(Long objectId, Long phoneId) {
        try {
            objectServiceClient.linkPhone(objectId, phoneId);
            return "";
        } catch (FeignException e) {
            log.error("ObjectEntity.linkPhone(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    public String unlinkPhone(Long objectId, Long phoneId) {
        try {
            objectServiceClient.unlinkPhone(objectId, phoneId);
            return "";
        } catch (FeignException e) {
            log.error("ObjectEntity.unlinkPhone(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }


    public String linkPerson(Long objectId, Long tabNo) {
        try {
            objectServiceClient.linkPerson(objectId, tabNo);
            return "";
        } catch (FeignException e) {
            log.error("ObjectEntity.linkPerson(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

    public String unlinkPerson(Long objectId, Long tabNo) {
        try {
            objectServiceClient.unlinkPerson(objectId, tabNo);
            return "";
        } catch (FeignException e) {
            log.error("ObjectEntity.unlinkPerson(): {}", e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return (e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
        }
    }

}
