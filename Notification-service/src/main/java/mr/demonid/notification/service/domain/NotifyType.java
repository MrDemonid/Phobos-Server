package mr.demonid.notification.service.domain;

public enum NotifyType {
    ALARM(125),      // тревога (125)
    TAKEN(119),      // взят под охрану(119)
    RELEASED(115);   // снят с охраны(115)

    private int code;

    NotifyType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static NotifyType fromCode(int code) {
        for (NotifyType type : NotifyType.values()) {
            if (type.code == code) {
                return type;
            }
        }
        return null;
    }

}
