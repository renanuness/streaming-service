package infra.subscription.persistence;

import infra.subscription.entity.SubscriptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionJpaEntity, String> {

    List<SubscriptionJpaEntity> findByUserId(String userId);

    @Query("SELECT s FROM SubscriptionJpaEntity s WHERE s.userId = :userId AND s.status = 'ACTIVE'")
    Optional<SubscriptionJpaEntity> findActiveByUserId(@Param("userId") String userId);
    List<SubscriptionJpaEntity> findByStatus(String status);

    @Query("SELECT COUNT(s) > 0 FROM SubscriptionJpaEntity s WHERE s.userId = :userId AND s.status = 'ACTIVE'")
    boolean hasActiveSubscription(@Param("userId") String userId);
    long countByStatus(String status);

    @Query("SELECT s FROM SubscriptionJpaEntity s WHERE s.expiresAt <= :date AND s.status = 'ACTIVE'")
    List<SubscriptionJpaEntity> findExpiringBefore(@Param("date") LocalDateTime date);
}