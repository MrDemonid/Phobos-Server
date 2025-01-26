package mr.demonid.storage.service.services;

import lombok.AllArgsConstructor;
import mr.demonid.storage.service.domain.WorkSchedule;
import mr.demonid.storage.service.dto.WorkScheduleDTO;
import mr.demonid.storage.service.exceptions.BadScheduleException;
import mr.demonid.storage.service.makers.WorkScheduleMaker;
import mr.demonid.storage.service.repository.WorkScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Класс для определения возможных режимов работы для цехов и персонала.
 */
@Service
@AllArgsConstructor
public class WorkScheduleService {

    private WorkScheduleRepository repository;
    private WorkScheduleMaker maker;

    /**
     * Возвращает режим по его id.
     */
    public WorkScheduleDTO getById(Long id) {
        try {
            Optional<WorkSchedule> schedule = repository.findByIdWithDependencies(id);
            if (schedule.isEmpty()) {
                throw new BadScheduleException("Такого режима не существует");
            }
            return maker.toDTO(schedule.get());

        } catch (Exception e) {
            throw new BadScheduleException(e.getMessage());
        }
    }

    /**
     * Возвращает список всех режимов.
     */
    public List<WorkScheduleDTO> getAll() {
        try {
            List<WorkSchedule> items = repository.findAllWithDependencies();
            return items.stream().map(maker::toDTO).collect(Collectors.toList());

        } catch (Exception e) {
            throw new BadScheduleException(e.getMessage());
        }
    }

    /**
     * Добавление нового режима работы, без зависимостей.
     */
    public WorkScheduleDTO create(WorkScheduleDTO request) {
        try {
            if (request.getId() != null) {
                throw new BadScheduleException("Режим с таким id уже существует!");
            }
            WorkSchedule schedule = maker.toSchedule(request);
            schedule.setObjects(new HashSet<>());
            schedule.setPersons(new HashSet<>());
            schedule = repository.save(schedule);
            return maker.toDTO(schedule);

        } catch (Exception e) {
            throw new BadScheduleException(e.getMessage());
        }
    }

    /**
     * Обновление режима работы.
     */
    @Transactional
    public WorkScheduleDTO update(WorkScheduleDTO request) {
        try {
            Optional<WorkSchedule> scheduleOptional = repository.findById(request.getId());
            if (scheduleOptional.isEmpty()) {
                throw new BadScheduleException("Такого режима не существует");
            }
            WorkSchedule schedule = maker.toSchedule(request);
            return maker.toDTO(repository.save(schedule));

        } catch (Exception e) {
            throw new BadScheduleException(e.getMessage());
        }
    }

    /**
     * Удаление режима работы.
     */
    public void delete(Long id) {
        try {
            Optional<WorkSchedule> schedule = repository.findById(id);
            if (schedule.isEmpty()) {
                throw new BadScheduleException("Такого режима не существует");
            }
            repository.deleteById(id);

        } catch (Exception e) {
            throw new BadScheduleException(e.getMessage());
        }
    }


}
