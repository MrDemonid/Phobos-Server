package mr.demonid.storage.service;

import mr.demonid.storage.service.domain.ObjectEntity;
import mr.demonid.storage.service.dto.ObjectEntityDTO;
import mr.demonid.storage.service.exceptions.BadObjectException;
import mr.demonid.storage.service.exceptions.BadPersonException;
import mr.demonid.storage.service.makers.ObjectMaker;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.services.ObjectEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;



/**
 * Модульные тесты.
 */
@ExtendWith(MockitoExtension.class)
public class ObjectEntityServiceTest {

    @Mock
    private ObjectEntityRepository objectRepository;

    @Mock
    private ObjectMaker objectMaker;

    @InjectMocks
    private ObjectEntityService objectEntityService;

    ObjectEntityDTO dto;
    ObjectEntityDTO dtoNull;
    ObjectEntity objectEntity;
    ObjectEntity objectEntityNull;
    /**
     * Подготавливаем данные для каждого тестового метода.
     */
    @BeforeEach
    public void setup() {
        dto = new ObjectEntityDTO(1L, "desc", "address", List.of(), Set.of(), Set.of(), Set.of());
        dtoNull = new ObjectEntityDTO(null, "desc", "address", List.of(), Set.of(), Set.of(), Set.of());
        objectEntity = new ObjectEntity(1L, "desc", "address", List.of(), Set.of(), Set.of(), Set.of());
        objectEntityNull = new ObjectEntity(null, "desc", "address", List.of(), Set.of(), Set.of(), Set.of());
    }

    /**
     * Сервис создания нового объекта.
     * Тестируем с корректным объектом.
     */
    @Test
    void createObjectEntityWithCorrectParams() {
        // ставим заглушки
        when(objectRepository.existsById(dto.getId())).thenReturn(false);
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(objectMaker.toDto(any(ObjectEntity.class))).thenReturn(dto);
        when(objectMaker.toObject(any(ObjectEntityDTO.class))).thenReturn(objectEntity);

        assertDoesNotThrow(() -> objectEntityService.create(dto));
        verify(objectRepository).save(any(ObjectEntity.class));
    }

    /**
     * Сервис создания нового объекта.
     * Тестируем с нулевым ID.
     */
    @Test
    void createObjectEntityWithBadId() {
        // ставим заглушки
        assertThrows(BadPersonException.class, () -> objectEntityService.create(dtoNull));
    }

    /**
     * Сервис создания нового объекта.
     * Тестируем с уже существующим пользователем в БД.
     */
    @Test
    void createObjectEntityWithUserPresent() {
        // ставим заглушки
        when(objectRepository.existsById(dto.getId())).thenReturn(true);
        assertThrows(BadObjectException.class, () -> objectEntityService.create(dto));
    }

    /**
     * Сервис создания нового объекта.
     * Тестируем с корректным объектом, но с ошибкой записи в БД.
     */
    @Test
    void createObjectEntityWithExternalErrorOnDBSave() {
        // ставим заглушки
        when(objectRepository.existsById(any())).thenReturn(false);
        when(objectRepository.save(any(ObjectEntity.class))).thenThrow(RuntimeException.class); // запись вызовет исключение
        when(objectMaker.toDto(any(ObjectEntity.class))).thenReturn(dto);

        assertThrows(BadObjectException.class, () -> objectEntityService.create(dto));
    }

    /*
        =============================================================================================
        Сервис обновления данных объекта.
        =============================================================================================
     */

    /**
     * Тестируем с корректными данными.
     *
     */
    @Test
    void updateObjectEntityWithCorrectParams() {
        when(objectRepository.findObjectDetails(dto.getId())).thenReturn(Optional.of(objectEntity));
        when(objectRepository.save(any(ObjectEntity.class))).thenReturn(objectEntity);
        when(objectMaker.toDto(any(ObjectEntity.class))).thenReturn(dto);
        when(objectMaker.toObject(any(ObjectEntityDTO.class))).thenReturn(objectEntity);

        assertDoesNotThrow(() -> objectEntityService.update(dto));
        verify(objectRepository).save(any(ObjectEntity.class));
    }

    /**
     * Тестируем с несуществующим объектом.
     */
    @Test
    void updateObjectEntityWithBadId() {
        when(objectRepository.findObjectDetails(any())).thenReturn(Optional.empty());
        assertThrows(BadObjectException.class, () -> objectEntityService.update(dtoNull));
    }

    /**
     * Тестируем с корректным объектом, но с ошибкой в БД.
     */
    @Test
    void updateObjectEntityWithExternalErrorOnDBUpdate() {
        when(objectRepository.findObjectDetails(dto.getId())).thenReturn(Optional.of(objectEntity));
        when(objectRepository.save(any(ObjectEntity.class))).thenThrow(RuntimeException.class);
        when(objectMaker.toDto(any(ObjectEntity.class))).thenReturn(dto);

        assertThrows(BadObjectException.class, () -> objectEntityService.update(dtoNull));
    }

    /*
        =============================================================================================
        Удаление объекта
        =============================================================================================
      */

    /**
     * Тестируем с корректными данными.
     */
    @Test
    void deleteObjectEntityWithCorrectParams() {
        when(objectRepository.existsById(dto.getId())).thenReturn(true);
        doNothing().when(objectRepository).deleteById(dto.getId());

        assertDoesNotThrow(() -> objectEntityService.delete(dto.getId()));
        verify(objectRepository).deleteById(dto.getId());
    }

    /**
     * Тестируем с некорректным Id.
     */
    @Test
    void deleteObjectEntityWithBadId() {
        when(objectRepository.existsById(any())).thenReturn(false);
        assertThrows(BadObjectException.class, () -> objectEntityService.delete(dtoNull.getId()));
    }

    /**
     * Тестируем с корректным объектом, но с ошибкой в БД.
     */
    @Test
    void deleteObjectEntityWithExternalErrorOnDBDelete() {
        when(objectRepository.existsById(dto.getId())).thenReturn(true);
        doThrow(RuntimeException.class).when(objectRepository).deleteById(dto.getId());

        assertThrows(BadObjectException.class, () -> objectEntityService.delete(dto.getId()));
    }

}
