package subscription.model.valueObject;

public enum TransactionStatus {
    PENDING("Pendente"),
    APPROVED("Aprovada"),
    DECLINED("Recusada"),
    REFUNDED("Estornada"),
    FAILED("Falhou"),
    CANCELLED("Cancelada");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }

    public String description() { return description; }

    public boolean isSuccessful() {
        return this == APPROVED;
    }
}

