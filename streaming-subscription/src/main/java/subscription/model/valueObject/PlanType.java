package subscription.model.valueObject;

public enum PlanType {
    BASIC("Básico", 1),
    PREMIUM("Premium", 1),
    FAMILIAR("Familiar", 5);

    private final String description;
    private final int maxMembers;

    PlanType(String description, int maxMembers) {
        this.description = description;
        this.maxMembers = maxMembers;
    }

    public String description() { return description; }
    public int maxMembers() { return maxMembers; }

    public boolean allowsMultipleMembers() {
        return this == FAMILIAR;
    }
}