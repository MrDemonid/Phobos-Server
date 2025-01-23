package mr.demonid.storage.service.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.storage.service.domain.*;
import mr.demonid.storage.service.dto.ObjectEntityDTO;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.exceptions.BadObjectException;
import mr.demonid.storage.service.exceptions.BadPersonException;
import mr.demonid.storage.service.exceptions.BadPhoneException;
import mr.demonid.storage.service.makers.ObjectMaker;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.PhoneRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.filters.ObjectEntitySpecifications;
import mr.demonid.storage.service.services.filters.ObjectFilter;
import mr.demonid.storage.service.services.filters.PersonFilter;
import mr.demonid.storage.service.services.filters.PersonSpecification;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import mr.demonid.storage.service.repository.ObjectEntityRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class ObjectEntityService {

    private final ObjectEntityRepository objectRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final PhoneRepository phoneRepository;
    private final PersonRepository personRepository;
    private ObjectMaker objectMaker;

    /**
     * Сервис создания нового объекта.
     */
    public ObjectEntityDTO create(ObjectEntityDTO objectEntityDTO) {
        if (objectEntityDTO.getId() == null) {
            throw new BadPersonException("Номер объекта не может быть пустым");
        }
        try {
            if (objectRepository.existsById(objectEntityDTO.getId())) {
                throw new BadObjectException("Такой объект уже существует");
            }
            return objectMaker.toDto(objectRepository.save(objectMaker.toObject(objectEntityDTO)));

        } catch (Exception e) {
            throw new BadObjectException(e.getMessage());
        }
    }

    /**
     * Сервис обновления данных объекта.
     */
    @Transactional
    public ObjectEntityDTO update(ObjectEntityDTO request) {
        try {
            Optional<ObjectEntity> objectEntity = objectRepository.findObjectDetails(request.getId());
            if (objectEntity.isEmpty()) {
                throw new BadObjectException("Такого объекта не существует");
            }
            return objectMaker.toDto(objectRepository.save(objectMaker.toObject(request)));

        } catch (Exception e) {
            throw new BadObjectException(e.getMessage());
        }
    }

    /**
     * Удаление объекта.
     */
    public void delete(Long id) {
        try {
            if (!objectRepository.existsById(id)) {
                throw new BadObjectException("Такого объекта не существует");
            }
            objectRepository.deleteById(id);

        } catch (Exception e) {
            throw new BadObjectException(e.getMessage());
        }
    }


    /**
     * Сервис по загрузке данных всех объектов.
     * Используется фильтр и разбиение на страницы.
     */
    @Transactional(readOnly = true)
    public Page<ObjectEntityDTO> getAllPageableWithFilter(ObjectFilter filter, Pageable pageable) {
        Page<ObjectEntity> items = objectRepository.findAll(ObjectEntitySpecifications.filterByCriteria(filter), pageable);
        return items.map(objectMaker::toDto);
    }


    /**
     * Сервис по загрузке данных об объекте.
     */
    @Transactional(readOnly = true)
    public ObjectEntityDTO getObjectById(Long objectId) {
        ObjectEntity object = loadObjectById(objectId);
        return objectMaker.toDto(object);
    }


    /**
     * Сервис добавления графика работы рабочему.
     */
    @Transactional
    public ObjectEntityDTO addSchedule(Long objectId, Long scheduleId) {
        ObjectEntity object = loadObjectById(objectId);
        WorkSchedule schedule = loadScheduleById(scheduleId);
        object.getWorkSchedules().add(schedule);
        schedule.getObjects().add(object);
        return  objectMaker.toDto(objectRepository.save(object));
    }

    /**
     * Сервис удаления графика работы сотрудника.
     */
    @Transactional
    public ObjectEntityDTO delSchedule(Long objectId, Long scheduleId) {
        ObjectEntity object = loadObjectById(objectId);
        WorkSchedule schedule = loadScheduleById(scheduleId);
        object.getWorkSchedules().remove(schedule);
        schedule.getObjects().remove(object);
        return objectMaker.toDto(objectRepository.save(object));
    }


    /**
     * Сервис привязки телефона к объекту.
     */
    @Transactional
    public ObjectEntityDTO linkPhone(Long objectId, Long phoneId) {
        Phone phone = loadPhoneById(phoneId);
        if (phone.getObject() != null) {
            log.error("ObjectEntity.linkPhone(): Телефон уже привязан к объекту {}", phone.getObject().getId());
            throw new BadPersonException("Телефон уже привязан к объекту " + phone.getObject().getId());
        }
        ObjectEntity object = loadObjectById(objectId);
        // создаем связь
        object.getPhones().add(phone);
        phone.setObject(object);
        phoneRepository.save(phone);
        object = objectRepository.save(object);
        log.info("ObjectEntity.linkPhone(): Телефон {} добавлен.", phone.getNumber());
        return objectMaker.toDto(object);
    }

    /**
     * Сервиз отвязки телефона от объекта.
     */
    @Transactional
    public ObjectEntityDTO unlinkPhone(Long objectId, Long phoneId) {
        Phone phone = loadPhoneById(phoneId);
        if (phone.getObject() == null || !phone.getObject().getId().equals(objectId)) {
            log.error("ObjectEntity.unlinkPhone(): Телефон привязан к другому объекту: {}", phone.getObject() == null ? "" : phone.getObject().getId());
            throw new BadPersonException("Телефон уже привязан к объекту: " + (phone.getObject() == null ? "" : phone.getObject().getId()));
        }
        ObjectEntity object = loadObjectById(objectId);
        object.getPhones().remove(phone);
        phone.setObject(null);
        phoneRepository.save(phone);
        object = objectRepository.save(object);
        log.info("ObjectEntity.unlinkPhone(): Телефон удален.");
        return objectMaker.toDto(object);
    }


    /**
     * Сервис добавления к объекту ответственного лица.
     */
    @Transactional
    public ObjectEntityDTO linkPerson(Long objectId, Long tabNo) {
        ObjectEntity object = loadObjectById(objectId);
        Person person = loadPersonById(tabNo);
        object.getPersons().add(person);
        person.getObjects().add(object);
        personRepository.save(person);
        object = objectRepository.save(object);
        return objectMaker.toDto(object);
    }

    /**
     * Сервис отвязки ответственного лица от объекта.
     */
    @Transactional
    public ObjectEntityDTO unlinkPerson(Long objectId, Long tabNo) {
        ObjectEntity object = loadObjectById(objectId);
        Person person = loadPersonById(tabNo);
        object.getPersons().remove(person);
        person.getObjects().remove(object);
        personRepository.save(person);
        object = objectRepository.save(object);
        return objectMaker.toDto(object);
    }

    /*===========================================================
    Вспомогательные утилиты.
     ===========================================================*/

    private ObjectEntity loadObjectById(Long id) throws BadObjectException {
        Optional<ObjectEntity> objectEntity = objectRepository.findObjectDetails(id);
        if (objectEntity.isEmpty()) {
            throw new BadObjectException("Такого объекта не существует");
        }
        return objectEntity.get();
    }

    private WorkSchedule loadScheduleById(Long scheduleId) throws BadPersonException {
        Optional<WorkSchedule> schedule = workScheduleRepository.findByIdWithDependencies(scheduleId);
        if (schedule.isEmpty()) {
            throw new BadPersonException("Такого графика работы не существует");
        }
        return schedule.get();
    }

    private Phone loadPhoneById(Long phoneId) throws BadPersonException {
        Optional<Phone> phone = phoneRepository.findByIdWithDependencies(phoneId);
        if (phone.isEmpty()) {
            throw new BadPersonException("Такого телефона не существует");
        }
        return phone.get();
    }

    private Person loadPersonById(Long tabNo) throws BadPersonException {
        Optional<Person> person = personRepository.findPersonDetails(tabNo);
        if (person.isEmpty()) {
            throw new BadPersonException("Такого сотрудника не существует");
        }
        return person.get();
    }

}
