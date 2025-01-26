package mr.demonid.storage.service;

import mr.demonid.storage.service.domain.Phone;
import mr.demonid.storage.service.domain.PhoneType;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.exceptions.BadPhoneException;
import mr.demonid.storage.service.makers.PhoneMaker;
import mr.demonid.storage.service.repository.PhoneRepository;
import mr.demonid.storage.service.services.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class PhoneServiceTest {

    @Mock
    PhoneMaker maker;
    @Mock
    PhoneRepository repository;
    @InjectMocks
    PhoneService service;

    PhoneDTO dto;
    PhoneDTO dtoFill;
    Phone phone;
    Phone phoneFill;


    @BeforeEach
    public void setup() {
        dto = new PhoneDTO(null, "63-22", PhoneType.WORK, "Кладовая", null, null);
        dtoFill = new PhoneDTO(1L, "63-22", PhoneType.WORK, "Кладовая", null, null);
        phone = new Phone(null, "63-22", PhoneType.WORK, "Кладовая", null, null);
        phoneFill = new Phone(1L, "63-22", PhoneType.WORK, "Кладовая", null, null);
    }

    /**
     * Добавление нового телефона.
     * На входе: корректные данные.
     */
    @Test
    public void createPhoneWithSuccess() {
        when(maker.dtoToPhone(dto)).thenReturn(phone);
        when(maker.phoneToDto(any(Phone.class))).thenReturn(dtoFill);
        when(repository.save(phone)).thenReturn(phoneFill);

        assertDoesNotThrow(() -> service.create(dto));
        verify(repository).save(phone);
        verify(maker).phoneToDto(phoneFill);
    }

    /**
     * Добавление нового телефона.
     * На входе: не корректный Id.
     */
    @Test
    public void createPhoneWithBadId() {
        assertThrows(BadPhoneException.class, () -> service.create(dtoFill));
    }

    /**
     * Добавление нового телефона.
     * На входе: корректные данные и ошибка в БД.
     */
    @Test
    public void createPhoneWithBadDB() {
        when(maker.dtoToPhone(dto)).thenReturn(phone);
        when(repository.save(phone)).thenThrow(RuntimeException.class);

        assertThrows(BadPhoneException.class, () -> service.create(dto));
        verify(repository).save(phone);
    }


    /**
     * Обновление данных телефона.
     * На входе: корректные данные.
     */
    @Test
    public void updatePhoneWithSuccess() {
        dtoFill.setNumber("63-63");
        dtoFill.setDescription("Кладовая ц.12");
        Phone phoneNew = new Phone();
        phoneNew.setId(dtoFill.getId());
        phoneNew.setType(dtoFill.getType());
        phoneNew.setNumber(dtoFill.getNumber());
        phoneNew.setDescription(dtoFill.getDescription());

        when(repository.findById(dtoFill.getId())).thenReturn(Optional.of(phoneFill));
        when(repository.save(phoneNew)).thenReturn(phoneNew);
        when(maker.phoneToDto(phoneNew)).thenReturn(dtoFill);

        assertDoesNotThrow(() -> service.update(dtoFill));
        verify(repository).save(phoneNew);
        verify(maker).phoneToDto(phoneNew);
    }

    /**
     * Обновление данных телефона.
     * На входе: не корректные данные.
     */
    @Test
    public void updatePhoneWithBadId() {
        assertThrows(BadPhoneException.class, () -> service.update(dto));
    }
    @Test
    public void updatePhoneWithPhoneNull() {
        assertThrows(BadPhoneException.class, () -> service.update(null));
    }

    /**
     * Обновление данных телефона.
     * На входе: корректные данные и ошибка в БД.
     */
    @Test
    public void updatePhoneWithBadDB() {
        dtoFill.setNumber("63-63");
        dtoFill.setDescription("Кладовая ц.12");
        Phone phoneNew = new Phone();
        phoneNew.setId(dtoFill.getId());
        phoneNew.setType(dtoFill.getType());
        phoneNew.setNumber(dtoFill.getNumber());
        phoneNew.setDescription(dtoFill.getDescription());

        when(repository.findById(dtoFill.getId())).thenReturn(Optional.of(phoneFill));
        when(repository.save(phoneNew)).thenThrow(RuntimeException.class);

        assertThrows(BadPhoneException.class, () -> service.update(dtoFill));
    }


    /**
     * Удаление телефона.
     * На входе: корректные данные.
     */
    @Test
    public void deletePhoneWithSuccess() {
        when(repository.findById(phoneFill.getId())).thenReturn(Optional.of(phoneFill));
        doNothing().when(repository).deleteById(phoneFill.getId());

        assertDoesNotThrow(() -> service.delete(phoneFill.getId()));
        verify(repository).deleteById(phoneFill.getId());
    }

    /**
     * Удаление телефона.
     * На входе: не корректные данные.
     */
    @Test
    public void deletePhoneWithBadId() {
        when(repository.findById(phoneFill.getId())).thenReturn(Optional.empty());
        assertThrows(BadPhoneException.class, () -> service.delete(null));
    }

    /**
     * Удаление телефона.
     * На входе: корректные данные и ошибка в БД.
     */
    @Test
    public void deletePhoneWithBadDB() {
        when(repository.findById(phoneFill.getId())).thenReturn(Optional.of(phoneFill));
        doThrow(RuntimeException.class).when(repository).deleteById(phoneFill.getId());

        assertThrows(BadPhoneException.class, () -> service.delete(phoneFill.getId()));
    }


}
