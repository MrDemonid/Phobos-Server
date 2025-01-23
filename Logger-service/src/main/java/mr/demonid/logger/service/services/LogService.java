package mr.demonid.logger.service.services;

import lombok.AllArgsConstructor;
import mr.demonid.logger.service.domain.LogOperation;
import mr.demonid.logger.service.domain.MessageStatus;
import mr.demonid.logger.service.dto.LogOperationRequest;
import mr.demonid.logger.service.exceptions.LoggerIOException;
import mr.demonid.logger.service.repository.LogRepository;
import mr.demonid.logger.service.services.filter.LogFilter;
import mr.demonid.logger.service.services.filter.LogSpecification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LogService {

    private LogRepository logRepository;

    /**
     * Отправляет в БД запись LogOperation
     */
    public Long putLog(LogOperationRequest log) {
        try {
            // создаем новую запись
            LogOperation operation = new LogOperation(log.getRepeater(), log.getKey(), log.getCode(), log.getLine(), log.getType(), log.getDate());
            operation = logRepository.save(operation);
            return operation.getId();
        } catch (Exception e) {
            throw new LoggerIOException("Ошибка записи в базу данных", e.getMessage());
        }
    }

    /**
     * Меняет статус записи с PENDING на SENT
     */
    public void confirm(Long id) {
        try {
            Optional<LogOperation> operation = logRepository.findById(id);
            if (operation.isPresent()) {
                LogOperation op = operation.get();
                op.setStatus(MessageStatus.SENT);
                logRepository.save(op);
            } else {
                throw new LoggerIOException("Ошибка обращения к базе данных", "Записи с ID: " + id + " не существует");
            }
        } catch (Exception e) {
            throw new LoggerIOException("Ошибка записи в базу данных", e.getMessage());
        }
    }

    /**
     * Выборка данных из БД в соответствии с заданным фильтром.
     */
    public Page<LogOperationRequest> getLogs(LogFilter filter, Pageable pageable) {
        try {
            Page<LogOperation> items = logRepository.findAll(LogSpecification.filterBy(filter), pageable);
            return items.map(e ->
                    new LogOperationRequest(
                            e.getRepeater(),
                            e.getKey(),
                            e.getCodeOp(),
                            e.getLineUO(),
                            e.getTypeUO(),
                            e.getDate())
            );
        } catch (Exception e) {
            throw new LoggerIOException("Ошибка чтения базы данных", e.getMessage());
        }
    }
}
