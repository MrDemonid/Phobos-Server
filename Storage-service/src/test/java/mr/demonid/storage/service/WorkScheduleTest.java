package mr.demonid.storage.service;

import mr.demonid.storage.service.domain.WorkSchedule;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.exceptions.BadScheduleException;
import mr.demonid.storage.service.makers.WorkScheduleMaker;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.WorkScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * Модульные тесты.
 */
@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
public class WorkScheduleTest {

    @Mock
    WorkScheduleMaker maker;
    @Mock
    WorkScheduleRepository repository;
    @InjectMocks
    WorkScheduleService service;

    WorkScheduleDTO dto;
    WorkScheduleDTO dtoNull;
    WorkSchedule workSchedule;

    /**
     * Подготавливаем данные для каждого тестового метода.
     */
    @BeforeEach
    public void setup() {
        dto = new WorkScheduleDTO(1L, "schedule details", Set.of(), Set.of());
        dtoNull = new WorkScheduleDTO(null, "schedule details", Set.of(), Set.of());
        workSchedule = new WorkSchedule(1L, "schedule details", Set.of(), Set.of());
    }


    /*
        ========================================================================
        Добавление нового режима работы.
        ========================================================================
     */
    /*
     * Тестируем с корректными параметрами.
     */
    @Test
    void createWorkScheduleWithCorrectData() {
        when(maker.toSchedule(any(WorkScheduleDTO.class))).thenReturn(workSchedule);
        when(repository.save(workSchedule)).thenReturn(workSchedule);
        when(maker.toDTO(workSchedule)).thenReturn(dto);

        assertDoesNotThrow(() -> service.create(dtoNull));
        verify(repository).save(workSchedule);
        verify(maker).toDTO(workSchedule);
    }

    /*
     * Тестируем с неверным Id
     */
    @Test
    void createWorkScheduleWithNullId() {
        assertThrows(BadScheduleException.class, () -> service.create(dto));
    }

    /*
     * Тестируем с ошибкой БД.
     */
    @Test
    void updateWorkScheduleWithBadDBSave() {
        when(maker.toSchedule(any(WorkScheduleDTO.class))).thenReturn(workSchedule);
        when(repository.save(workSchedule)).thenThrow(BadScheduleException.class);

        assertThrows(BadScheduleException.class, () -> service.create(dtoNull));
        verify(repository).save(workSchedule);
    }


    /*
        ========================================================================
        Обновление существующего режима
        ========================================================================
     */
    /*
     * Тестируем с корректными параметрами.
     */
    @Test
    void updateWorkScheduleWithCorrectData() {
        when(repository.findById(workSchedule.getId())).thenReturn(Optional.of(workSchedule));
        when(maker.toSchedule(any(WorkScheduleDTO.class))).thenReturn(workSchedule);
        when(repository.save(workSchedule)).thenReturn(workSchedule);
        when(maker.toDTO(workSchedule)).thenReturn(dto);

        assertDoesNotThrow(() -> service.update(dto));
        verify(repository).save(workSchedule);
    }

    /*
     * Тестируем с неверным Id
     */
    @Test
    void updateWorkScheduleWithNullId() {
        when(repository.findById(workSchedule.getId())).thenReturn(Optional.empty());

        assertThrows(BadScheduleException.class, () -> service.update(dto));
    }

    /*
     * Тестируем с ошибкой БД.
     */
    @Test
    void updateWorkScheduleWithBadDBUpdate() {
        when(repository.findById(workSchedule.getId())).thenReturn(Optional.of(workSchedule));
        when(maker.toSchedule(any(WorkScheduleDTO.class))).thenReturn(workSchedule);
        when(repository.save(workSchedule)).thenThrow(BadScheduleException.class);

        assertThrows(BadScheduleException.class, () -> service.update(dto));
    }


    /*
        ========================================================================
        Удаление существующего режима
        ========================================================================
     */
    /*
     * Тестируем с корректными параметрами.
     */
    @Test
    void deleteWorkScheduleWithCorrectData() {
        when(repository.findById(dto.getId())).thenReturn(Optional.of(workSchedule));

        assertDoesNotThrow(() -> service.delete(dto.getId()));
        verify(repository).deleteById(workSchedule.getId());
    }

    /*
     * Тестируем с некорректным Id
     */
    @Test
    void deleteWorkScheduleWithNullId() {
        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(BadScheduleException.class, () -> service.delete(dto.getId()));
    }

    /*
     * Тестируем с корректным объектом, но с ошибкой в БД.
     */
    @Test
    void deleteWorkScheduleWithBadDBDelete() {
        when(repository.findById(dto.getId())).thenReturn(Optional.of(workSchedule));
        doThrow(BadScheduleException.class).when(repository).deleteById(workSchedule.getId());

        assertThrows(BadScheduleException.class, () -> service.delete(dto.getId()));
        verify(repository).deleteById(workSchedule.getId());
    }

}
