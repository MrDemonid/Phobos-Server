package mr.demonid.web.client.service.filters;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Фильтр для выборки данных из БД микросервиса Logger.
 */
@Data
@AllArgsConstructor
public class LogFilter {
    private int repeater;           // 1-8 - номер ретранслятора, < 0 - не используется
    private int key;                // направление (ключ), < 0 - не используется
    private LocalDateTime from;     // null - берутся сутки, отсчитывая от поля 'to'
    private LocalDateTime to;       // null - берется текущая дата и время
}
