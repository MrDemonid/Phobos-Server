package mr.demonid.rs232dev.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private int repeater;       // номер ретранслятора
    private int key;            // номер направления (ключа)
    private int code;           // код сообщений (команды)
    private int line;           // номер шлейфа сигнализации (-1 если не используется)
    private int type;           // тип УО, или номер служебного сообщения (-1 если не используется)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.n]")
    private LocalDateTime date;
}
