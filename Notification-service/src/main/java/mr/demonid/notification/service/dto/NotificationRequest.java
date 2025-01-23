package mr.demonid.notification.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

//@Data
//@AllArgsConstructor
public class NotificationRequest {
    private int repeater;       // номер ретранслятора
    private int key;            // номер направления (ключа)
    private int code;           // код сообщений (команды)
    private int line;           // номер шлейфа сигнализации (-1 если не используется)
    private int type;           // тип УО, или номер служебного сообщения (-1 если не используется)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.n]")
    private LocalDateTime date;

    private boolean isLogger;   // доставлено ли в Logger
    private boolean success;    // доставлено ли адресату (оператору)

    public NotificationRequest(int repeater, int key, int code, int line, int type, LocalDateTime date, boolean isLogger, boolean success) {
        this.repeater = repeater;
        this.key = key;
        this.code = code;
        this.line = line;
        this.type = type;
        this.date = date;
        this.isLogger = isLogger;
        this.success = success;
    }

    public int getRepeater() {
        return repeater;
    }

    public void setRepeater(int repeater) {
        this.repeater = repeater;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isLogger() {
        return isLogger;
    }

    public void setLogger(boolean logger) {
        isLogger = logger;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "NotificationRequest{" +
                "repeater=" + repeater +
                ", key=" + key +
                ", code=" + code +
                ", line=" + line +
                ", type=" + type +
                ", date=" + date +
                ", isLogger=" + isLogger +
                ", success=" + success +
                '}';
    }
}
