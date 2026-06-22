package subscription.model.valueObject;

public enum TransactionType {
    PAYMENT("Pagamento"),
    REFUND("Estorno"),
    CHARGEBACK("Contestação");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String description() { return description; }
}


