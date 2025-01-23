package mr.demonid.storage.service.domain;

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
}
