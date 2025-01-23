package mr.demonid.web.client.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.web.client.dto.PageDTO;
import mr.demonid.web.client.service.filters.LogFilter;
import mr.demonid.web.client.dto.LogOperationDTO;
import mr.demonid.web.client.links.LoggerServiceClient;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Сервис по взаимодействию с микросервисом Logger.
 */
@Service
@AllArgsConstructor
@Log4j2
public class LoggerService {

    LoggerServiceClient loggerServiceClient;

    /**
     * Запрос лога операций между оператором и оборудованием.
     */
    public PageDTO<LogOperationDTO> getLogOperationsWithFilter(LogFilter filter, Pageable pageable) {
        try {
            return loggerServiceClient.getLog(filter, pageable).getBody();
        } catch (FeignException e) {
            log.error(e.contentUTF8().isBlank() ? e.getMessage() : e.contentUTF8());
            return PageDTO.empty();
        }
    }

}
