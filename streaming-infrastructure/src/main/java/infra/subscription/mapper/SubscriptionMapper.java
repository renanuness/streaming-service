package infra.subscription.mapper;

import shared.model.valueObject.UserId;
import subscription.model.aggregate.Subscription;
import subscription.model.entity.Transaction;
import subscription.model.valueObject.*;
import infra.subscription.entity.SubscriptionJpaEntity;
import infra.subscription.entity.TransactionJpaEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionMapper {

    private final ObjectMapper objectMapper;

    public SubscriptionMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public Subscription toDomainSubscription(SubscriptionJpaEntity entity) {
        SubscriptionId id = new SubscriptionId(entity.getId());
        UserId userId = new UserId(entity.getUserId());

        PlanType planType = PlanType.valueOf(entity.getPlanType());
        Money price = new Money(entity.getPlanPrice(), Currency.getInstance("BRL"));
        PlanDuration duration = new PlanDuration(
                entity.getPlanDurationAmount(),
                PlanDuration.DurationUnit.valueOf(entity.getPlanDurationUnit())
        );
        Plan plan = getPlanByType(planType);
        SubscriptionStatus status = SubscriptionStatus.valueOf(entity.getStatus());

        CreditCard creditCard = null;
        if (entity.getCardLastFour() != null) {
            creditCard = new CreditCard(
                    entity.getCardLastFour(),
                    entity.getCardHolderName(),
                    YearMonth.parse(entity.getCardExpiration()),
                    CardBrand.valueOf(entity.getCardBrand()),
                    CardStatus.valueOf(entity.getCardStatus())
            );
        }

        List<Transaction> transactions = entity.getTransactions().stream()
                .map(this::toDomainTransaction)
                .collect(Collectors.toList());

        List<UserId> familyMembers = parseFamilyMembers(entity.getFamilyMembers());

        return Subscription.reconstruct(
                id, userId, plan, status, creditCard, transactions,
                familyMembers, entity.getStartedAt(), entity.getExpiresAt(),
                entity.getCancelledAt()
        );
    }

    public SubscriptionJpaEntity toJpaEntity(Subscription subscription) {
        SubscriptionJpaEntity entity = new SubscriptionJpaEntity();

        entity.setId(subscription.id().value());
        entity.setUserId(subscription.userId().value());
        entity.setPlanType(subscription.plan().type().name());
        entity.setPlanPrice(subscription.plan().price().amount());
        entity.setPlanDurationAmount(subscription.plan().duration().amount());
        entity.setPlanDurationUnit(subscription.plan().duration().unit().name());
        entity.setStatus(subscription.status().name());

        if (subscription.creditCard() != null) {
            entity.setCardLastFour(subscription.creditCard().lastFourDigits());
            entity.setCardHolderName(subscription.creditCard().holderName());
            entity.setCardExpiration(subscription.creditCard().expirationDate().toString());
            entity.setCardBrand(subscription.creditCard().brand().name());
            entity.setCardStatus(subscription.creditCard().status().name());
        }

        List<TransactionJpaEntity> transactionEntities = subscription.transactions()
                .stream()
                .map(t -> toJpaTransaction(t, entity.getId()))
                .collect(Collectors.toList());
        entity.setTransactions(transactionEntities);

        entity.setFamilyMembers(serializeFamilyMembers(subscription.familyMembers()));

        entity.setStartedAt(subscription.startedAt());
        entity.setExpiresAt(subscription.expiresAt());
        entity.setCancelledAt(subscription.cancelledAt());
        entity.setCreatedAt(subscription.createdAt());
        entity.setUpdatedAt(subscription.updatedAt());

        return entity;
    }

    private Transaction toDomainTransaction(TransactionJpaEntity entity) {
        return Transaction.reconstruct(
                new TransactionId(entity.getId()),
                new Money(entity.getAmount(), Currency.getInstance(entity.getCurrency())),
                entity.getMerchant(),
                TransactionType.valueOf(entity.getType()),
                TransactionStatus.valueOf(entity.getStatus()),
                entity.getTimestamp(),
                entity.getFailureReason()
        );
    }

    private TransactionJpaEntity toJpaTransaction(Transaction transaction, String subscriptionId) {
        TransactionJpaEntity entity = new TransactionJpaEntity();
        entity.setId(transaction.id().value());
        entity.setSubscriptionId(subscriptionId);
        entity.setAmount(transaction.amount().amount());
        entity.setCurrency(transaction.amount().currency().getCurrencyCode());
        entity.setMerchant(transaction.merchant());
        entity.setType(transaction.type().name());
        entity.setStatus(transaction.status().name());
        entity.setTimestamp(transaction.timestamp());
        entity.setFailureReason(transaction.failureReason());
        return entity;
    }

    private List<UserId> parseFamilyMembers(String json) {
        try {
            if (json == null || json.isBlank()) {
                return Collections.emptyList();
            }
            List<String> memberIds = objectMapper.readValue(json, new TypeReference<List<String>>() {});
            return memberIds.stream().map(UserId::new).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private String serializeFamilyMembers(List<UserId> members) {
        try {
            List<String> memberIds = members.stream()
                    .map(UserId::value)
                    .collect(Collectors.toList());
            return objectMapper.writeValueAsString(memberIds);
        } catch (Exception e) {
            return "[]";
        }
    }

    private Plan getPlanByType(PlanType type) {
        return switch (type) {
            case BASIC -> Plan.basic();
            case PREMIUM -> Plan.premium();
            case FAMILIAR -> Plan.familiar();
        };
    }
}