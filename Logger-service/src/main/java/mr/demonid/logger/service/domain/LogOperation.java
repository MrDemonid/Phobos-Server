package mr.demonid.logger.service.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Структура операции обмена данными между оператором и оборудованием,
 * которую и будем сохранять в БД.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "log_operation")
public class LogOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "repeater", nullable = false)
    private int repeater;

    @Column(name = "key_obj", nullable = false)
    private int key;

    @Column(name = "code_op", nullable = false)
    private int codeOp;

    @Column(name = "param1", nullable = true)
    private int lineUO;

    @Column(name = "param2", nullable = true)
    private int typeUO;

    @Column(name = "date_op", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.n]")
    private LocalDateTime date = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private MessageStatus status;


    public LogOperation(int repeater, int key, int codeOp, int lineUO, int typeUO, LocalDateTime date) {
        this.id = null;
        this.repeater = repeater;
        this.key = key;
        this.codeOp = codeOp;
        this.lineUO = lineUO;
        this.typeUO = typeUO;
        this.date = date;
        this.status = MessageStatus.PENDING;
    }
}
