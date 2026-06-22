package subscription.model.valueObject;

import shared.exception.BusinessRuleException;

public record PlanDuration(int amount, DurationUnit unit) {

    public PlanDuration {
        if (amount <= 0) {
            throw new BusinessRuleException("Duração deve ser positiva");
        }
        if (unit == null) {
            throw new BusinessRuleException("Unidade de duração não pode ser nula");
        }
    }

    public static PlanDuration monthly() {
        return new PlanDuration(1, DurationUnit.MONTH);
    }

    public static PlanDuration yearly() {
        return new PlanDuration(1, DurationUnit.YEAR);
    }

    public enum DurationUnit {
        DAY, MONTH, YEAR
    }

    @Override
    public String toString() {
        return amount + " " + unit.name().toLowerCase() + (amount > 1 ? "s" : "");
    }
}