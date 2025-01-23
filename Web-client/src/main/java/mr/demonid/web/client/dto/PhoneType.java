package mr.demonid.web.client.dto;


import java.util.Arrays;
import java.util.List;

public enum PhoneType {
    WORK("Рабочий"),
    MOBILE("Мобильный"),
    HOME("Домашний"),
    LINE_ONLY("Линия без тлф.");

    private String description;

    PhoneType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public static List<PhoneType> getAllTypes() {
        return Arrays.asList(values());
    }
}
