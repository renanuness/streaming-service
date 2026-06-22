package infra.subscription.persistence;
import subscription.model.aggregate.Subscription;
import subscription.model.valueObject.SubscriptionId;
import subscription.model.valueObject.SubscriptionStatus;
import shared.model.valueObject.UserId;
import subscription.repository.SubscriptionRepository;
import infra.subscription.entity.SubscriptionJpaEntity;
import infra.subscription.mapper.SubscriptionMapper;
import infra.subscription.persistence.SubscriptionJpaRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaSubscriptionRepository implements SubscriptionRepository {

    private final SubscriptionJpaRepository jpaRepository;
    private final SubscriptionMapper mapper;

    public JpaSubscriptionRepository(
            SubscriptionJpaRepository jpaRepository,
            SubscriptionMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Subscription subscription) {
        SubscriptionJpaEntity entity = mapper.toJpaEntity(subscription);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Subscription> findById(SubscriptionId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomainSubscription);
    }

    @Override
    public Optional<Subscription> findActiveByUserId(UserId userId) {
        return jpaRepository.findActiveByUserId(userId.value())
                .map(mapper::toDomainSubscription);
    }

    @Override
    public List<Subscription> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.value())
                .stream()
                .map(mapper::toDomainSubscription)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findByStatus(SubscriptionStatus status) {
        return jpaRepository.findByStatus(status.name())
                .stream()
                .map(mapper::toDomainSubscription)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasActiveSubscription(UserId userId) {
        return jpaRepository.hasActiveSubscription(userId.value());
    }

    @Override
    public int countActiveSubscriptions() {
        return (int) jpaRepository.countByStatus("ACTIVE");
    }

    @Override
    public List<Subscription> findExpiringBefore(LocalDateTime date) {
        return jpaRepository.findExpiringBefore(date)
                .stream()
                .map(mapper::toDomainSubscription)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Subscription subscription) {
        jpaRepository.deleteById(subscription.id().value());
    }
}