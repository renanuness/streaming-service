package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.Money;
import subscription.model.valueObject.PlanType;
import subscription.model.valueObject.SubscriptionId;

public class SubscriptionRenewedEvent extends DomainEvent {
    private final String subscriptionId;
    private final String userId;
    private final String planType;

    public SubscriptionRenewedEvent(SubscriptionId subscriptionId, UserId userId,
                                      PlanType planType) {
        super();
        this.subscriptionId = subscriptionId.value();
        this.userId = userId.value();
        this.planType = planType.name();
    }

    public String subscriptionId() { return subscriptionId; }
    public String userId() { return userId; }
    public String planType() { return planType; }

    @Override
    public String eventType() {
        return "subscription.renewed";
    }
}
