package mr.demonid.storage.service;

import mr.demonid.storage.service.domain.WorkSchedule;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.exceptions.BadScheduleException;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.WorkScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Проверяем работу сервиса WorkScheduleService
 */
@SpringBootTest
@ActiveProfiles("test")
public class WorkScheduleIntegrationTest {

    // репозитории
    @Autowired
    private WorkScheduleRepository repository;
    @Autowired
    private ObjectEntityRepository objectEntityRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    WorkScheduleService service;

    WorkSchedule workSchedule;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
        // заносим в БД тестовые данные
        workSchedule = repository.save(new WorkSchedule(null, "test schedule", null, null));
    }

    /**
     * Тест добавления объекта в базу данных.
     * На входе корректные данные.
     */
    @Test
    public void workScheduleCreateWithSuccess() {
        WorkScheduleDTO dto = new WorkScheduleDTO(null, "test details", null, null);

        WorkScheduleDTO res = service.create(dto);

        assertNotNull(res);
        assertEquals(dto.getScheduleDetails(), res.getScheduleDetails());
    }

    /**
     * Тест добавления объекта в базу данных.
     * На входе уже существующий объект.
     */
    @Test
    public void workScheduleCreateWithError() {
        assertThrows(BadScheduleException.class, () -> service.create(new WorkScheduleDTO(workSchedule.getId(), "test details", null, null)));
    }

    /**
     * Тест обновления объекта в базе данных.
     * На входе существующий объект.
     */
    @Test
    public void workScheduleUpdateWithSuccess() {
        WorkScheduleDTO dto = new WorkScheduleDTO(workSchedule.getId(), "new test details", null, null);

        WorkScheduleDTO res = service.update(dto);

        assertNotNull(res);
        assertEquals(dto.getScheduleDetails(), res.getScheduleDetails());
        assertEquals(workSchedule.getId(), res.getId());
    }

    /**
     * Тест обновления объекта в базе данных.
     * На входе не существующий объект.
     */
    @Test
    public void workScheduleUpdateWithError() {
        assertThrows(BadScheduleException.class, () -> {service.delete(null);});
    }

    /**
     * Тест удаления объекта из базы данных.
     * На входе существующий объект.
     */
    @Test
    public void workScheduleDeleteWithSuccess() {
        assertDoesNotThrow(() -> service.delete(workSchedule.getId()));

        WorkSchedule res = repository.findById(workSchedule.getId()).orElse(null);
        assertNull(res);
    }

    /**
     * Тест удаления объекта из базы данных.
     * На входе не существующий объект.
     */
    @Test
    public void workScheduleDeleteWithError() {
        assertThrows(BadScheduleException.class, () -> {service.delete(null);});
    }

}
