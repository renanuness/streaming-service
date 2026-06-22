package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.*;

public class SubscriptionActivatedEvent extends DomainEvent {
    private final String subscriptionId;
    private final String userId;
    private final String planType;
    private final Money price;

    public SubscriptionActivatedEvent(SubscriptionId subscriptionId, UserId userId,
                                      PlanType planType, Money price) {
        super();
        this.subscriptionId = subscriptionId.value();
        this.userId = userId.value();
        this.planType = planType.name();
        this.price = price;
    }

    public String subscriptionId() { return subscriptionId; }
    public String userId() { return userId; }
    public String planType() { return planType; }
    public Money price() { return price; }

    @Override
    public String eventType() {
        return "subscription.activated";
    }
}

