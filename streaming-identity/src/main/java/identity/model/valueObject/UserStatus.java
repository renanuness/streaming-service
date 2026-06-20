package identity.model.valueObject;

public enum UserStatus {
    ACTIVE("Ativo"),
    SUSPENDED("Suspenso"),
    DELETED("Excluído");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
