package mr.demonid.web.client.dto;

import java.util.Arrays;
import java.util.List;

public enum GenderType {
    MALE ("мужской"),
    FEMALE ("женский"),
    UNKNOWN ("не определено");

    String description;

    GenderType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<GenderType> getAllTypes() {
        return Arrays.asList(values());
    }

}
