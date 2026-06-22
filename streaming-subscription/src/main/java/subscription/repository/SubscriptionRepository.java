package subscription.repository;

import shared.model.valueObject.UserId;
import subscription.model.aggregate.Subscription;
import subscription.model.valueObject.SubscriptionId;
import subscription.model.valueObject.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository {
    void save(Subscription subscription);
    Optional<Subscription> findById(SubscriptionId id);
    Optional<Subscription> findActiveByUserId(UserId userId);
    List<Subscription> findByUserId(UserId userId);
    List<Subscription> findByStatus(SubscriptionStatus status);
    boolean hasActiveSubscription(UserId userId);
    int countActiveSubscriptions();
    List<Subscription> findExpiringBefore(LocalDateTime date);
    void delete(Subscription subscription);
}