package subscription.model.valueObject;

public enum CardBrand {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    ELO("Elo"),
    AMERICAN_EXPRESS("American Express"),
    HIPERCARD("Hipercard");

    private final String displayName;

    CardBrand(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { return displayName; }
}