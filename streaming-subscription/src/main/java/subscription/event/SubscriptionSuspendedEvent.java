package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.SubscriptionId;

public class SubscriptionSuspendedEvent extends DomainEvent {
    private SubscriptionId id;
    private UserId userId;
    private String reason;

    public SubscriptionSuspendedEvent(SubscriptionId id, UserId userId, String reason) {
        this.id = id;
        this.userId = userId;
        this.reason =reason;
    }

    @Override
    public String eventType() {
        return "subscription.suspended";
    }
}
