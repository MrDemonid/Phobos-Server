package mr.demonid.storage.service.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.storage.service.domain.Address;
import mr.demonid.storage.service.domain.Person;
import mr.demonid.storage.service.domain.Phone;
import mr.demonid.storage.service.domain.WorkSchedule;
import mr.demonid.storage.service.dto.AddressDTO;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.exceptions.BadPersonException;
import mr.demonid.storage.service.exceptions.BadPhoneException;
import mr.demonid.storage.service.makers.PersonMaker;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.PhoneRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.filters.PersonFilter;
import mr.demonid.storage.service.services.filters.PersonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Log4j2
public class PersonService {

    private PersonRepository personRepository;
    private PhoneRepository phoneRepository;
    private WorkScheduleRepository workScheduleRepository;
    private PersonMaker personMaker;


    /**
     * Сервис создания нового сотрудника.
     */
    public PersonDTO create(PersonDTO person) {
        try {
            if (person.getTabNo() == null || person.getTabNo() < 0) {
                throw new BadPersonException("Табельный номер не может быть пустым");
            }
            if (personRepository.findByTabNo(person.getTabNo()) != null) {
                throw new BadPersonException("Такой сотрудник уже существует: " + personRepository.findByTabNo(person.getTabNo()));
            }
            return personMaker.toDto(personRepository.save(personMaker.toPerson(person)), null);

        } catch (Exception e) {
            throw new BadPersonException(e.getMessage());
        }

    }

    /**
     * Сервис обновления данных сотрудника.
     */
    @Transactional
    public PersonDTO update(PersonDTO request) {
        try {
            Optional<Person> personOptional = personRepository.findPersonDetails(request.getTabNo());
            if (personOptional.isEmpty()) {
                throw new BadPhoneException("Такого пользователя не существует");
            }
            Person person = personMaker.toPerson(request);
            return personMaker.toDto(personRepository.save(person), request.getObjects());

        } catch (Exception e) {
            throw new BadPersonException(e.getMessage());
        }
    }

    /**
     * Удаление пользователя.
     */
    public void delete(Long tabNo) throws BadPersonException {
        try {
            if (personRepository.findByTabNo(tabNo) == null) {
                throw new BadPersonException("Такого пользователя не существует");
            }
            personRepository.deleteById(tabNo);
        } catch (Exception e) {
            throw new BadPersonException(e.getMessage());
        }
    }

    /**
     * Сервис по загрузке данных всех сотрудников.
     * Используется фильтр и разбиение на страницы.
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> getAllPageableWithFilter(PersonFilter filter, Pageable pageable) {
        Page<Person> items = personRepository.findAll(PersonSpecification.filterBy(filter), pageable);

        return items.map(t -> personMaker.toDto(t, loadPersonIdsByTabNo(t.getTabNo())));
    }


    /**
     * Сервис по загрузке данных о сотруднике.
     */
    @Transactional(readOnly = true)
    public PersonDTO getPersonByTabNo(Long tabNo) {
        Person person = loadPersonByTabNo(tabNo);
        return personMaker.toDto(person, loadPersonIdsByTabNo(tabNo));
    }


    /**
     * Сервис поиска сотрудников по фамилии, с выборкой в первые 10 записей.
     */
    public List<PersonDTO> searchPersons(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new BadPersonException("Недопустимый шаблон для поиска");
        }
        try {
            List<Person> persons = personRepository.findAllWithLimit10(PageRequest.of(0, 10), "%" + lastName + "%");
            return persons.stream().map(p -> personMaker.toDto(p, null)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new BadPersonException(e.getMessage());
        }
    }

    /**
     * Сервис привязки телефона к сотруднику.
     */
    @Transactional
    public PersonDTO linkPhone(Long tabNo, Long phoneId) {
        Person person = loadPersonByTabNo(tabNo);
        Phone phone = loadPhoneById(phoneId);
        if (phone.getPerson() != null) {
            log.error("linkPhone(): Телефон уже привязан к пользователю с таб. номером №{}", phone.getPerson().getTabNo());
            throw new BadPersonException("Телефон уже привязан к пользователю с таб. номером: " + phone.getPerson().getTabNo());
        }
        // создаем связь
        person.getPhones().add(phone);
        phone.setPerson(person);
        person = personRepository.save(person);
        phone = phoneRepository.findByIdWithDependencies(phoneId).orElse(null);
        log.info("linkPhone(): Телефон добавлен. Person.phones[] = {}, Phone.person = {}", person.getPhones(), phone == null ? "" : phone.getPerson());
        return personMaker.toDto(person, loadPersonIdsByTabNo(tabNo));
    }

    /**
     * Сервиз отвязки телефона от сотрудника.
     */
    @Transactional
    public PersonDTO unlinkPhone(Long tabNo, Long phoneId) {
        Person person = loadPersonByTabNo(tabNo);
        Phone phone = loadPhoneById(phoneId);
        if (phone.getPerson() == null || !phone.getPerson().getTabNo().equals(tabNo)) {
            log.error("unlinkPhone(): Телефон привязан к другому пользователю с таб. номером №{}", phone.getPerson() == null ? "" : phone.getPerson().getTabNo());
            throw new BadPersonException("Телефон уже привязан к пользователю с таб. номером: " + (phone.getPerson() == null ? "" : phone.getPerson().getTabNo()));
        }
        // удаляем связь
        person.getPhones().remove(phone);
        phone.setPerson(null);
        person = personRepository.save(person);
        phone = phoneRepository.findByIdWithDependencies(phoneId).orElse(null);
        log.info("unlinkPhone(): Телефон удален. Person.phones[] = {}, Phone.person = {}", person.getPhones(), phone == null ? "" : phone.getPerson());
        return personMaker.toDto(person, loadPersonIdsByTabNo(tabNo));
    }


    /**
     * Сервис добавления адреса сотруднику.
     */
    @Transactional
    public PersonDTO addAddress(Long tabNo, AddressDTO addr) {
        Person person = loadPersonByTabNo(tabNo);
        person.getAddresses().add(new Address(addr.getCity(), addr.getStreet(), addr.getPostalCode(), addr.getDescription()));
        person = personRepository.save(person);
        return personMaker.toDto(person, loadPersonIdsByTabNo(tabNo));
    }

    /**
     * Сервис удаления адреса у сотрудника.
     */
    @Transactional
    public PersonDTO delAddress(Long tabNo, AddressDTO addr) {
        Person person = loadPersonByTabNo(tabNo);
        Address address = new Address(addr.getCity(), addr.getStreet(), addr.getPostalCode(), addr.getDescription());
        person.getAddresses().remove(address);
        return personMaker.toDto(personRepository.save(person), loadPersonIdsByTabNo(tabNo));
    }


    /**
     * Сервис добавления графика работы рабочему.
     */
    @Transactional
    public PersonDTO addSchedule(Long tabNo, Long scheduleId) {
        Person person = loadPersonByTabNo(tabNo);
        WorkSchedule schedule = loadScheduleById(scheduleId);
        person.getWorkSchedules().add(schedule);
        schedule.getPersons().add(person);
        return personMaker.toDto(personRepository.save(person), loadPersonIdsByTabNo(tabNo));
    }

    /**
     * Сервис удаления графика работы сотрудника.
     */
    @Transactional
    public PersonDTO delSchedule(Long tabNo, Long scheduleId) {
        Person person = loadPersonByTabNo(tabNo);
        WorkSchedule schedule = loadScheduleById(scheduleId);
        person.getWorkSchedules().remove(schedule);
        schedule.getPersons().remove(person);
        return personMaker.toDto(personRepository.save(person), loadPersonIdsByTabNo(tabNo));
    }

    /**
     * Создает множество Id объектов, к которым привязан Person.
     */
    public Set<Long> loadPersonIdsByTabNo(Long tabNo) {
        return personRepository.findObjectIdsByPersonTabNo(tabNo);
    }

    /*===========================================================
        Вспомогательные утилиты.
     ===========================================================*/

    private Person loadPersonByTabNo(Long tabNo) throws BadPersonException {
        Optional<Person> personOptional = personRepository.findPersonDetails(tabNo);
        if (personOptional.isEmpty()) {
            throw new BadPersonException("Такого пользователя не существует");
        }
        return personOptional.get();
    }

    private Phone loadPhoneById(Long id) throws BadPersonException {
        Optional<Phone> phoneOptional = phoneRepository.findByIdWithDependencies(id);
        if (phoneOptional.isEmpty()) {
            throw new BadPersonException("Такого телефона не существует");
        }
        return phoneOptional.get();
    }

    private WorkSchedule loadScheduleById(Long scheduleId) throws BadPersonException {
        Optional<WorkSchedule> schedule = workScheduleRepository.findByIdWithDependencies(scheduleId);
        if (schedule.isEmpty()) {
            throw new BadPersonException("Такого графика работы не существует");
        }
        return schedule.get();
    }

}
