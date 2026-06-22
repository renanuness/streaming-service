package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.SubscriptionId;

public class FamilyMemberAddedEvent extends DomainEvent {
    private SubscriptionId id;
    private UserId userId;
    private UserId memberId;

    public FamilyMemberAddedEvent(SubscriptionId id, UserId userId, UserId memberId) {
        this.id = id;
        this.userId = userId;
        this.memberId = memberId;
    }

    @Override
    public String eventType() {
        return "subscription.familyMemberAdded";
    }
}
