package mr.demonid.logger.service.services.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Фильтр для выборки данных из БД.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogFilter {
    private int repeater;           // < 0 - не используется
    private int key;                // < 0 - не используется
    private LocalDateTime from;     // null - берутся сутки, отсчитывая от поля 'to'
    private LocalDateTime to;       // null - берется текущая дата и время
}
