package mr.demonid.storage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mr.demonid.storage.service.domain.GenderType;
import mr.demonid.storage.service.domain.Person;
import mr.demonid.storage.service.domain.Phone;
import mr.demonid.storage.service.domain.PhoneType;
import mr.demonid.storage.service.dto.PhoneDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.PhoneRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.PhoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Проверяем работу REST-контроллера PersonController.
 * Для тестов подключаем базу данных H2 и эмитируем контекст безопасности.
 */
@SpringBootTest
@ActiveProfiles("test")
public class PhoneIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    // репозитории
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    @Autowired
    private ObjectEntityRepository objectEntityRepository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PhoneRepository repository;

    @Autowired
    private PhoneService service;

    Phone phone;

    // Инициализация каждого теста
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        repository.deleteAll();
        phone = repository.save(new Phone(null, "63-63", PhoneType.WORK, "Кладовая", null, null));
    }


    /**
     * Тестирует эндпоинт создания нового телефона.
     * На входе: корректные параметры.
     */
    @Test
    public void createPhoneWithSuccess() throws Exception {
        PhoneDTO dto = new PhoneDTO(null, "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(post("/api/storage/phones/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.number").value(dto.getNumber()))
                .andExpect(jsonPath("$.type").value(dto.getType().toString()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    /**
     * Тестирует эндпоинт создания нового телефона.
     * На входе: не корректные параметры.
     */
    @Test
    public void createPhoneWithBadId() throws Exception {
        PhoneDTO dto = new PhoneDTO(-1L, "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(post("/api/storage/phones/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void createPhoneWithAlreadyPhone() throws Exception {
        PhoneDTO dto = new PhoneDTO(phone.getId(), "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(post("/api/storage/phones/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }


    /**
     * Тестирует эндпоинт создания нового телефона.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void createPhoneWithAnonymousUser() throws Exception {
        PhoneDTO dto = new PhoneDTO(null, "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(post("/api/storage/phones/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт создания нового телефона.
     * На входе: не авторизированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void createPhoneWithForbidden() throws Exception {
        PhoneDTO dto = new PhoneDTO(null, "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(post("/api/storage/phones/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());
    }


    /**
     * Тест обновление телефона в базе данных.
     * На входе: корректные данные.
     */
    @Test
    public void updatePhoneWithSuccess() throws Exception {
        PhoneDTO dto = new PhoneDTO(phone.getId(), "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(put("/api/storage/phones/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.number").value(dto.getNumber()))
                .andExpect(jsonPath("$.type").value(dto.getType().toString()))
                .andExpect(jsonPath("$.description").value(dto.getDescription()));
    }

    /**
     * Тест обновление телефона в базе данных.
     * На входе: не корректные данные.
     */
    @Test
    public void updatePhoneWithBadId() throws Exception {
        PhoneDTO dto = new PhoneDTO(-1L, "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(put("/api/storage/phones/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void updatePhoneWithNullId() throws Exception {
        PhoneDTO dto = new PhoneDTO(null, "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(put("/api/storage/phones/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест обновление телефона в базе данных.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void updatePhoneWithAnonymousUser() throws Exception {
        PhoneDTO dto = new PhoneDTO(phone.getId(), "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(put("/api/storage/phones/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тест обновление телефона в базе данных.
     * На входе: не авторизированный пользователь.
     */
    @Test
    public void updatePhoneWithForbidden() throws Exception {
        PhoneDTO dto = new PhoneDTO(phone.getId(), "63-59", PhoneType.WORK, "Отдел энергетиков", null, null);

        mockMvc.perform(put("/api/storage/phones/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());
    }


    /**
     * Тест удаления телефона из базы данных.
     * На входе: корректные данные.
     */
    @Test
    public void deletePhoneWithSuccess() throws Exception {
        assertTrue(repository.existsById(phone.getId()));

        mockMvc.perform(delete("/api/storage/phones/delete?id=" + phone.getId())
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                )))
                .andExpect(status().is2xxSuccessful());
        assertFalse(repository.existsById(phone.getId()));
    }

    /**
     * Тест удаления телефона из базы данных.
     * На входе: не корректные данные.
     */
    @Test
    public void deletePhoneWithBadId() throws Exception {
        mockMvc.perform(delete("/api/storage/phones/delete?id=-1")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                )))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void deletePhoneWithNullId() throws Exception {
        mockMvc.perform(delete("/api/storage/phones/delete?id=")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                )))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void deletePhoneWithIdIsZero() throws Exception {
        mockMvc.perform(delete("/api/storage/phones/delete?id=0")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                )))
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест удаления телефона из базы данных.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void deletePhoneWithAnonymousUser() throws Exception {

        mockMvc.perform(delete("/api/storage/phones/delete?id=" + phone.getId())
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тест удаления телефона из базы данных.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void deletePhoneWithForbidden() throws Exception {

        mockMvc.perform(delete("/api/storage/phones/delete?id=" + phone.getId())
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                )))
                .andExpect(status().isForbidden());
    }


}
