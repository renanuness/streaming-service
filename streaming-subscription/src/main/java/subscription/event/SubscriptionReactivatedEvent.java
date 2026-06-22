package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.SubscriptionId;

public class SubscriptionReactivatedEvent extends DomainEvent {
    private SubscriptionId id;
    private UserId userId;

    public SubscriptionReactivatedEvent(SubscriptionId id, UserId userId) {
        this.id = id;
        this. userId = userId;
    }

    @Override
    public String eventType() {
        return "subscription.reactivated";
    }
}
