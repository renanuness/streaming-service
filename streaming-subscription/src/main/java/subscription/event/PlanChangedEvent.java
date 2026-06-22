package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.PlanType;
import subscription.model.valueObject.SubscriptionId;

public class PlanChangedEvent extends DomainEvent {
    private final String subscriptionId;
    private final String userId;
    private final String oldPlan;
    private final String newPlan;

    public PlanChangedEvent(SubscriptionId subscriptionId, UserId userId, PlanType oldPlan, PlanType newPlan) {
        this.subscriptionId = subscriptionId.value();
        this.userId = userId.value();
        this.oldPlan = oldPlan.description();
        this.newPlan = newPlan.description();
    }

    public String subscriptionId() { return subscriptionId; }
    public String userId() { return userId; }
    public String oldPlan() { return oldPlan; }
    public String newPlan() { return newPlan; }

    @Override
    public String eventType() {
        return "subscription.plan.changed";
    }
}
