package mr.demonid.storage.service;

import mr.demonid.storage.service.domain.GenderType;
import mr.demonid.storage.service.domain.Person;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.exceptions.BadPersonException;
import mr.demonid.storage.service.makers.PersonMaker;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Модульные тесты.
 * Проверяем работу логики отдельных изолированных методов.
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @Mock
    private PersonMaker maker;

    @InjectMocks
    private PersonService service;

    PersonDTO dto;
    PersonDTO dtoNull;
    Person person;
    Person personNull;


    /**
     * Подготавливаем данные для каждого тестового метода.
     */
    @BeforeEach
    public void setup() {
        dto = new PersonDTO(86L, "Ivan", "Trubanov", "Alexandrovich", LocalDate.of(1975, Month.SEPTEMBER, 6), GenderType.MALE, null, null, null, null, null);
        dtoNull = new PersonDTO(null, "Ivan", "Trubanov", "Alexandrovich", LocalDate.of(1975, Month.SEPTEMBER, 6), GenderType.MALE, null, null, null, null, null);
        person = new Person(86L, "Ivan", "Trubanov", "Alexandrovich", LocalDate.of(1975, Month.SEPTEMBER, 6), GenderType.MALE, null, null, null, null, null);
        personNull = new Person(null, "Ivan", "Trubanov", "Alexandrovich", LocalDate.of(1975, Month.SEPTEMBER, 6), GenderType.MALE, null, null, null, null, null);
    }


    /**
     * Сервис создания нового сотрудника.
     * На входе: корректные данные.
     */
    @Test
    public void createPersonWithSuccess() {
        when(repository.findByTabNo(dto.getTabNo())).thenReturn(null);
        when(maker.toPerson(dto)).thenReturn(person);
        when(repository.save(person)).thenReturn(person);
        when(maker.toDto(person, null)).thenReturn(dto);

        assertDoesNotThrow(() -> service.create(dto));
        verify(repository).save(person);
    }

    /**
     * Сервис создания нового сотрудника.
     * На входе: не корректные данные.
     */
    @Test
    public void createPersonWithBadId() {
        assertThrows(BadPersonException.class, () -> service.create(dtoNull));
    }

    @Test
    public void createPersonWithPersonIsNull() {
        assertThrows(BadPersonException.class, () -> service.create(null));
    }

    /**
     * Сервис создания нового сотрудника.
     * На входе: корректные данные и ошибка в БД.
     */
    @Test
    public void createPersonWithBadDB() {
        when(repository.findByTabNo(dto.getTabNo())).thenReturn(null);
        when(maker.toPerson(dto)).thenReturn(person);
        when(repository.save(person)).thenThrow(RuntimeException.class);
        assertThrows(BadPersonException.class, () -> service.create(dto));
    }


    /**
     * Сервис обновления данных сотрудника.
     * На входе: корректные данные.
     */
    @Test
    public void updatePersonWithSuccess() {
        when(repository.findPersonDetails(person.getTabNo())).thenReturn(Optional.of(person));
        when(maker.toPerson(dto)).thenReturn(person);
        when(maker.toDto(person, dto.getObjects())).thenReturn(dto);
        when(repository.save(person)).thenReturn(person);

        assertDoesNotThrow(() -> service.update(dto));
        verify(repository).save(person);
    }

    /**
     * Сервис обновления данных сотрудника.
     * На входе: не корректные данные.
     */
    @Test
    public void updatePersonWithBadId() {
        assertThrows(BadPersonException.class, () -> service.update(dtoNull));
    }
    @Test
    public void updatePersonWithPersonNull() {
        assertThrows(BadPersonException.class, () -> service.update(null));
    }

    /**
     * Сервис обновления данных сотрудника.
     * На входе: корректные данные и ошибка в БД.
     */
    @Test
    public void updatePersonWithBadDB() {
        when(repository.findPersonDetails(person.getTabNo())).thenReturn(Optional.of(person));
        when(maker.toPerson(dto)).thenReturn(person);
        when(repository.save(person)).thenThrow(RuntimeException.class);

        assertThrows(BadPersonException.class, () -> service.update(dto));
    }


    /**
     * Удаление пользователя.
     * На входе: корректные данные.
     */
    @Test
    public void deleteDeleteWithSuccess() {
        when(repository.findByTabNo(dto.getTabNo())).thenReturn(person);
        doNothing().when(repository).deleteById(dto.getTabNo());

        assertDoesNotThrow(() -> service.delete(dto.getTabNo()));
        verify(repository).deleteById(dto.getTabNo());
    }

    /**
     * Удаление пользователя.
     * На входе: не корректные данные.
     */
    @Test
    public void deleteDeleteWithBadId() {
        when(repository.findByTabNo(dto.getTabNo())).thenReturn(null);
        assertThrows(BadPersonException.class, () -> service.delete(null));
    }

    /**
     * Удаление пользователя.
     * На входе: корректные данные и ошибка в БД.
     */
    @Test
    public void deleteDeleteWithBadDB() {
        when(repository.findByTabNo(dto.getTabNo())).thenReturn(person);
        doThrow(RuntimeException.class).when(repository).deleteById(dto.getTabNo());

        assertThrows(BadPersonException.class, () -> service.delete(dto.getTabNo()));
    }

}
