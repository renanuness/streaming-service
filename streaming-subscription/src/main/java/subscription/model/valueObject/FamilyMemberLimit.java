package subscription.model.valueObject;

import shared.exception.BusinessRuleException;

public record FamilyMemberLimit(int maxMembers) {

    private static final int MIN_MEMBERS = 1;
    private static final int MAX_MEMBERS = 5;

    public FamilyMemberLimit {
        if (maxMembers < MIN_MEMBERS || maxMembers > MAX_MEMBERS) {
            throw new BusinessRuleException(
                    String.format("Limite de membros deve ser entre %d e %d", MIN_MEMBERS, MAX_MEMBERS)
            );
        }
    }

    public static FamilyMemberLimit standard() {
        return new FamilyMemberLimit(5);
    }
}
