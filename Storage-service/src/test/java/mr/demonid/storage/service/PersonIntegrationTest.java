package mr.demonid.storage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import mr.demonid.storage.service.domain.GenderType;
import mr.demonid.storage.service.domain.Person;
import mr.demonid.storage.service.dto.PersonDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
public class PersonIntegrationTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;  // придется заменить, поскольку родной не поддерживает LocalDate

    private MockMvc mockMvc;

    // репозитории
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    @Autowired
    private ObjectEntityRepository objectEntityRepository;
    @Autowired
    private PersonRepository repository;

    @Autowired
    PersonService service;

    private Person person;


    // Инициализация каждого теста
    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        repository.deleteAll();
        person = repository.save(new Person(1L, "Иван", "Чонкин", "Батькович", LocalDate.of(1975, Month.SEPTEMBER, 6), GenderType.MALE, null, null, null, null, null));
    }


    /**
     * Тестирует эндпоинт создания нового сотрудника.
     * На входе: корректные параметры.
     */
    @Test
    public void createPersonWithSuccess() throws Exception {
        PersonDTO dto = new PersonDTO(43L, "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(post("/api/storage/persons/create")
                .with(user("Andrey")
                        .authorities(
                                new SimpleGrantedAuthority("SCOPE_write"),
                                new SimpleGrantedAuthority("ROLE_USER")
                        ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.tabNo").value(dto.getTabNo()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.middleName").value(dto.getMiddleName()))
                .andExpect(jsonPath("$.birthDate").value(dto.getBirthDate().toString()))
                .andExpect(jsonPath("$.gender").value(GenderType.FEMALE.toString()));
    }

    /**
     * Тестирует эндпоинт создания нового сотрудника.
     * На входе: не корректные параметры.
     */
    @Test
    public void createPersonWithBadId() throws Exception {
        PersonDTO dto = new PersonDTO(-1L, "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(post("/api/storage/persons/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тестирует эндпоинт создания нового сотрудника.
     * На входе: не аутентифицированный пользователь
     */
    @Test
    @WithAnonymousUser
    public void createPersonWithAnonymousUser() throws Exception {
        PersonDTO dto = new PersonDTO(43L, "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(post("/api/storage/persons/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт создания нового сотрудника.
     * На входе: не авторизированный пользователь.
     */
    @Test
    public void createPersonWithForbidden() throws Exception {
        PersonDTO dto = new PersonDTO(43L, "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(post("/api/storage/persons/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());
    }


    /**
     * Тестирует эндпоинт обновления данных сотрудника
     * На входе: корректные данные.
     */
    @Test
    public void updatePersonWithSuccess() throws Exception {
        PersonDTO dto = new PersonDTO(person.getTabNo(), "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(put("/api/storage/persons/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.tabNo").value(dto.getTabNo()))
                .andExpect(jsonPath("$.firstName").value(dto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(dto.getLastName()))
                .andExpect(jsonPath("$.middleName").value(dto.getMiddleName()))
                .andExpect(jsonPath("$.birthDate").value(dto.getBirthDate().toString()))
                .andExpect(jsonPath("$.gender").value(GenderType.FEMALE.toString()));

    }

    /**
     * Тестирует эндпоинт обновления данных сотрудника
     * На входе: не корректные данные.
     */
    @Test
    public void updatePersonWithBadId() throws Exception {
        PersonDTO dto = new PersonDTO(-1L, "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(put("/api/storage/persons/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тестирует эндпоинт обновления данных сотрудника
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void updatePersonWithAnonymousUser() throws Exception {
        PersonDTO dto = new PersonDTO(person.getTabNo(), "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(put("/api/storage/persons/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт обновления данных сотрудника
     * На входе: не авторизированный пользователь.
     */
    @Test
    public void updatePersonWithForbidden() throws Exception {
        PersonDTO dto = new PersonDTO(person.getTabNo(), "Мария", "Трифонова", "Батьковна", LocalDate.of(1980, Month.APRIL, 23), GenderType.FEMALE, null, null, null, null, null);

        mockMvc.perform(put("/api/storage/persons/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());
    }


    /**
     * Тестирует эндпоинт удаления сотрудника.
     * На входе: корректные данные.
     */
    @Test
    public void deletePersonWithSuccess() throws Exception {
        assertTrue(repository.existsById(person.getTabNo()));
        mockMvc.perform(delete("/api/storage/persons/delete/" + person.getTabNo())
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().is2xxSuccessful());
        assertFalse(repository.existsById(person.getTabNo()));
    }

    /**
     * Тестирует эндпоинт удаления сотрудника.
     * На входе: не корректные данные.
     */
    @Test
    public void deletePersonWithBadId() throws Exception {
        mockMvc.perform(delete("/api/storage/persons/delete/-1")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().isBadRequest());
    }


    /**
     * Тестирует эндпоинт удаления сотрудника.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void deletePersonWithAnonymousUser() throws Exception {
        mockMvc.perform(delete("/api/storage/persons/delete/" + person.getTabNo())
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт удаления сотрудника.
     * На входе: не авторизированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void deletePersonWithForbidden() throws Exception {
        mockMvc.perform(delete("/api/storage/persons/delete/" + person.getTabNo())
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().isForbidden());
    }

}
