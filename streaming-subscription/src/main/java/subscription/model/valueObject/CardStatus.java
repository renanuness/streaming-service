package subscription.model.valueObject;

public enum CardStatus {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    EXPIRED("Expirado"),
    BLOCKED("Bloqueado"),
    CANCELLED("Cancelado");

    private final String description;

    CardStatus(String description) {
        this.description = description;
    }

    public String description() { return description; }

    public boolean canProcessPayment() {
        return this == ACTIVE;
    }
}