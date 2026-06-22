package subscription.model.valueObject;

import shared.exception.BusinessRuleException;

public enum SubscriptionStatus {
    PENDING("Pendente"),
    ACTIVE("Ativa"),
    SUSPENDED("Suspensa"),
    CANCELLED("Cancelada"),
    EXPIRED("Expirada");

    private final String description;

    SubscriptionStatus(String description) {
        this.description = description;
    }

    public String description() { return description; }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canBeCancelled() {
        return this == ACTIVE || this == SUSPENDED;
    }

    public boolean canBeActivated() {
        return this == PENDING || this == SUSPENDED;
    }
}


