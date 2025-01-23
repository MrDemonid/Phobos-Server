package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.WorkScheduleDTO;
import mr.demonid.web.client.exceptions.WorkScheduleException;
import mr.demonid.web.client.links.WorkScheduleClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для взаимодействия БД WorkSchedule, микросервиса Storage.
 * Все методы (за исключением оговоренных отдельно), в случае ошибок,
 * генерируют исключение WorkScheduleException.
 */
@Service
@AllArgsConstructor
@Log4j2
public class WorkScheduleService {

    private WorkScheduleClient workScheduleClient;


    /**
     * Получение списка всех режимов работы.
     * В случае ошибки возвращает пустой список, поскольку результат идет
     * не в REST-контроллер, а в простой веб-контроллер и не доходит до клиента.
     */
    public List<WorkScheduleDTO> getWorkSchedules() {
        try {
            return workScheduleClient.getAll().getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            return new ArrayList<>();
        }
    }

    /**
     * Создание нового режима.
     */
    public WorkScheduleDTO createWorkSchedule(WorkScheduleDTO workScheduleDTO) {
        try {
            return workScheduleClient.create(workScheduleDTO).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new WorkScheduleException(error);
        }
    }

    /**
     * Обновление данных существующего расписания.
     */
    public WorkScheduleDTO updateWorkSchedule(WorkScheduleDTO workScheduleDTO) {
        try {
            return workScheduleClient.update(workScheduleDTO).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new WorkScheduleException(error);
        }
    }

    /**
     * Удаление записи расписания по его ID.
     */
    public void deleteWorkSchedule(Long id) {
        try {
            workScheduleClient.delete(id).getBody();
        } catch (FeignException e) {
            String error = e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8();
            log.error(error);
            throw new WorkScheduleException(error);
        }
    }
}
