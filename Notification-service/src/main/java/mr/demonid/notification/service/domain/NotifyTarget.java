package mr.demonid.notification.service.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Уведомления ответственным лицам.
 */
@Entity
//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
public class NotifyTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // триггеры уведомления
    @Column(name = "direction", nullable = false)
    private Integer key;

    @Column(nullable = false)
    private String email;

    // тип уведомления
    @Enumerated(EnumType.STRING)
    NotifyType notificationType;


    public NotifyTarget() {
    }

    public NotifyTarget(Integer key, String email, NotifyType notificationType) {
        this.id = null;
        this.key = key;
        this.email = email;
        this.notificationType = notificationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NotifyType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotifyType notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public String toString() {
        return "NotifyTarget{" +
                "id=" + id +
                ", key=" + key +
                ", email='" + email + '\'' +
                ", notificationType=" + notificationType +
                '}';
    }
}
