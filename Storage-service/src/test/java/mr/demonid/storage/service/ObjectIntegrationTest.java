package mr.demonid.storage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import mr.demonid.storage.service.domain.ObjectEntity;
import mr.demonid.storage.service.dto.ObjectEntityDTO;
import mr.demonid.storage.service.repository.ObjectEntityRepository;
import mr.demonid.storage.service.repository.PersonRepository;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import mr.demonid.storage.service.services.ObjectEntityService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Проверяем работу REST-контроллера ObjectController.
 * Для тестов подключаем базу данных H2 и эмитируем контекст безопасности.
 */
@SpringBootTest
@ActiveProfiles("test")
public class ObjectIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    // репозитории
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    @Autowired
    private ObjectEntityRepository repository;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectEntityService service;

    private ObjectEntity objectEntity;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        repository.deleteAll();
        // заносим в БД тестовые данные
        objectEntity = repository.save(new ObjectEntity(50L, "description", "K-5", null, null, null, null));
    }

    /**
     * Тестирует эндпоинт создания нового объекта.
     * На входе: корректные параметры.
     */
    @Test
    public void objectCreateWithSuccess() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(43L, "new description", "K-7", null, null, null, null);

        mockMvc.perform(post("/api/storage/objects/create")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_write"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(43L))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andExpect(jsonPath("$.address").value(dto.getAddress()));
    }

    /**
     * Тестирует эндпоинт создания нового объекта.
     * На входе: не корректный Id.
     */
    @Test
    public void objectCreateBadId() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(null, "new description", "K-7", null, null, null, null);

        mockMvc.perform(post("/api/storage/objects/create")
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
     * Тестирует эндпоинт создания нового объекта.
     * На входе: не аутентифицированный пользователь
     */
    @Test
    @WithAnonymousUser
    public void objectCreateIfNotAuthenticated() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(43L, "new description", "K-7", null, null, null, null);

        mockMvc.perform(post("/api/storage/objects/create")
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт создания нового объекта.
     * На входе: не авторизированный пользователь
     */
    @Test
    public void objectCreateIfNotAuthorized() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(43L, "new description", "K-7", null, null, null, null);

        mockMvc.perform(post("/api/storage/objects/create")
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
     * Тестирует эндпоинт обновления данных объекта
     * На входе: корректные данные.
     */
    @Test
    public void objectUpdateWithSuccess() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(50L, "new description", "K-7", null, null, null, null);

        mockMvc.perform(put("/api/storage/objects/update")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_update"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(50L))
                .andExpect(jsonPath("$.description").value(dto.getDescription()))
                .andExpect(jsonPath("$.address").value(dto.getAddress()));
    }

    /**
     * Тестирует эндпоинт обновления данных объекта
     * На входе: не корректный Id.
     */
    @Test
    public void objectUpdateWithBadId() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(43L, "new description", "K-7", null, null, null, null);

        mockMvc.perform(put("/api/storage/objects/update")
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
     * Тестирует эндпоинт обновления данных объекта
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void objectUpdateIfNotAuthenticated() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(50L, "new description", "K-7", null, null, null, null);

        mockMvc.perform(put("/api/storage/objects/update")
                        .contentType(MediaType.APPLICATION_JSON) // тип передаваемых данных
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт обновления данных объекта
     * На входе: не авторизированный пользователь.
     */
    @Test
    public void objectUpdateIfNotAuthorized() throws Exception {
        ObjectEntityDTO dto = new ObjectEntityDTO(50L, "new description", "K-7", null, null, null, null);
        mockMvc.perform(put("/api/storage/objects/update")
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
     * Тестирует эндпоинт удаления объекта.
     * На входе: корректные данные.
     */
    @Test
    public void objectDeleteWithSuccess() throws Exception {
        mockMvc.perform(delete("/api/storage/objects/delete/" + objectEntity.getId())
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().is2xxSuccessful());
        assertFalse(repository.existsById(objectEntity.getId()));
    }

    /**
     * Тестирует эндпоинт удаления объекта.
     * На входе: не корректный Id.
     */
    @Test
    public void objectDeleteWithBadId() throws Exception {
        mockMvc.perform(delete("/api/storage/objects/delete/65535")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void objectDeleteWithSignId() throws Exception {
        mockMvc.perform(delete("/api/storage/objects/delete/-1")
                        .with(user("Andrey")
                                .authorities(
                                        new SimpleGrantedAuthority("SCOPE_delete"),
                                        new SimpleGrantedAuthority("ROLE_USER")
                                ))
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Тестирует эндпоинт удаления объекта.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    @WithAnonymousUser
    public void objectDeleteWithNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/api/storage/objects/delete/" + objectEntity.getId())
                )
                .andExpect(status().isUnauthorized());
    }

    /**
     * Тестирует эндпоинт удаления объекта.
     * На входе: не аутентифицированный пользователь.
     */
    @Test
    public void objectDeleteWithNotAuthorized() throws Exception {
        mockMvc.perform(delete("/api/storage/objects/delete/" + objectEntity.getId())
                .with(user("Andrey")
                        .authorities(
                                new SimpleGrantedAuthority("ROLE_USER")
                        ))
        )
                .andExpect(status().isForbidden());
    }


}
