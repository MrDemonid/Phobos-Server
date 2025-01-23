package mr.demonid.logger.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import mr.demonid.logger.service.domain.LogOperation;

import java.time.LocalDateTime;


/**
 * Данные для клиентов.
 */
@Data
@AllArgsConstructor
public class LogOperationRequest {
    private int repeater;       // номер ретранслятора
    private int key;            // номер направления (ключа)
    private int code;           // код сообщений (команды)
    private int line;           // номер шлейфа сигнализации (-1 если не используется)
    private int type;           // тип УО, или номер служебного сообщения (-1 если не используется)
    private LocalDateTime date;
}
