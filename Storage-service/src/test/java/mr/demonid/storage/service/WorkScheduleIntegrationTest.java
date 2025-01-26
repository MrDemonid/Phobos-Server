package mr.demonid.storage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import mr.demonid.storage.service.domain.WorkSchedule;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.WorkScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Проверяем работу сервиса WorkScheduleService
 */
@SpringBootTest
@ActiveProfiles("test")
public class WorkScheduleIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

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
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        repository.deleteAll();
        // заносим в БД тестовые данные
        workSchedule = repository.save(new WorkSchedule(null, "test schedule", null, null));
    }

    /**
     * Тест добавления объекта в базу данных.
     * На входе: корректные данные.
     */
    @Test
    public void testCreateWorkSchedule() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(null, "test details", null, null);

        mockMvc.perform(post("/api/storage/schedule")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.scheduleDetails").value(dto.getScheduleDetails()));
    }

    /**
     * Тест добавления объекта в базу данных.
     * На входе: уже существующий объект.
     */
    @Test
    public void testCreateWorkScheduleWithBadId() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(workSchedule.getId(), "new test details", null, null);

        mockMvc.perform(post("/api/storage/schedule")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест добавления объекта в базу данных.
     * На входе: не аутентифицированный пользователь
     */
    @Test
    @WithAnonymousUser
    public void testCreateWorkScheduleWithAnonymousUser() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(null, "test details", null, null);

        mockMvc.perform(post("/api/storage/schedule")
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тест добавления объекта в базу данных.
     * На входе: не авторизированный пользователь
     */
    @Test
    public void createWorkScheduleWithForbidden() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(null, "test details", null, null);

        mockMvc.perform(post("/api/storage/schedule")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());
    }


    /**
     * Тест обновления объекта в базе данных.
     * На входе: корректные данные.
     */
    @Test
    public void updateWorkScheduleWithSuccess() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(workSchedule.getId(), "new test details", null, null);

        mockMvc.perform(put("/api/storage/schedule")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.scheduleDetails").value(dto.getScheduleDetails()));
    }

    /**
     * Тест обновления объекта в базе данных.
     * На входе: не существующий объект.
     */
    @Test
    public void updateWorkScheduleWithBadId() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(null, "new test details", null, null);

        mockMvc.perform(put("/api/storage/schedule")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест обновления объекта в базе данных.
     * На входе: не аутентифицированный пользователь
     */
    @Test
    @WithAnonymousUser
    public void updateWorkScheduleWithAnonymousUser() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(workSchedule.getId(), "new test details", null, null);

        mockMvc.perform(put("/api/storage/schedule")
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тест обновления объекта в базе данных.
     * На входе: не авторизированный пользователь
     */
    @Test
    public void updateWorkScheduleWithForbidden() throws Exception {
        WorkScheduleDTO dto = new WorkScheduleDTO(workSchedule.getId(), "new test details", null, null);

        mockMvc.perform(put("/api/storage/schedule")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isForbidden());
    }



    /**
     * Тест удаления объекта из базы данных.
     * На входе: корректные данные.
     */
    @Test
    public void deleteWorkScheduleWithSuccess() throws Exception {
        mockMvc.perform(delete("/api/storage/schedule?id=" + workSchedule.getId())
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().is2xxSuccessful());
        assertFalse(repository.existsById(workSchedule.getId()));
    }

    /**
     * Тест удаления объекта из базы данных.
     * На входе: не существующий объект.
     */
    @Test
    public void deleteWorkScheduleWithBadId() throws Exception {
        mockMvc.perform(delete("/api/storage/schedule?id=")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тест удаления объекта из базы данных.
     * На входе: не аутентифицированный пользователь
     */
    @Test
    @WithAnonymousUser
    public void deleteWorkScheduleWithAnonymousUser() throws Exception {
        mockMvc.perform(delete("/api/storage/schedule?id=" + workSchedule.getId()))
                .andExpect(status().isUnauthorized());
        assertTrue(repository.existsById(workSchedule.getId()));
    }

    /**
     * Тест удаления объекта из базы данных.
     * На входе: не авторизированный пользователь
     */
    @Test
    @WithAnonymousUser
    public void deleteWorkScheduleWithForbidden() throws Exception {
        mockMvc.perform(delete("/api/storage/schedule?id=" + workSchedule.getId())
                .with(user("Andrey")
                        .authorities(
                                new SimpleGrantedAuthority("ROLE_USER")
                        ))
                )
                .andExpect(status().isForbidden());
        assertTrue(repository.existsById(workSchedule.getId()));
    }





}
