package mr.demonid.web.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Запрос к Logger, о получении протокола обмена между оператором и оборудованием.
 */
@Data
@AllArgsConstructor
public class LogOperationDTO {
    private int repeater;       // номер ретранслятора
    private int key;            // номер направления (ключа)
    private int code;           // код сообщений (команды)
    private int line;           // номер шлейфа сигнализации (-1 если не используется)
    private int type;           // тип УО, или номер служебного сообщения (-1 если не используется)
    private LocalDateTime date;

    public String formatDateTime() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

}
