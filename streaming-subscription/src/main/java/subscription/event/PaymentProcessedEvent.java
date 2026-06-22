package subscription.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;
import subscription.model.valueObject.Money;
import subscription.model.valueObject.SubscriptionId;
import subscription.model.valueObject.TransactionId;

public class PaymentProcessedEvent extends DomainEvent {
    private final String subscriptionId;
    private final String userId;
    private final String transactionId;
    private final Money amount;

    public PaymentProcessedEvent(SubscriptionId subscriptionId, UserId userId,
                                 TransactionId transactionId, Money amount) {
        super();
        this.subscriptionId = subscriptionId.value();
        this.userId = userId.value();
        this.transactionId = transactionId.value();
        this.amount = amount;
    }

    public String subscriptionId() { return subscriptionId; }
    public String userId() { return userId; }
    public String transactionId() { return transactionId; }
    public Money amount() { return amount; }

    @Override
    public String eventType() {
        return "subscription.payment.processed";
    }
}


