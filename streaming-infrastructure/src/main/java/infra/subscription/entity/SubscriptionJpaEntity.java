package infra.subscription.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subscriptions")
public class SubscriptionJpaEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "plan_type", nullable = false, length = 20)
    private String planType;

    @Column(name = "plan_price", nullable = false)
    private BigDecimal planPrice;

    @Column(name = "plan_duration_amount", nullable = false)
    private int planDurationAmount;

    @Column(name = "plan_duration_unit", nullable = false, length = 10)
    private String planDurationUnit;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "card_last_four", length = 4)
    private String cardLastFour;

    @Column(name = "card_holder_name", length = 100)
    private String cardHolderName;

    @Column(name = "card_expiration", length = 7)
    private String cardExpiration;

    @Column(name = "card_brand", length = 20)
    private String cardBrand;

    @Column(name = "card_status", length = 20)
    private String cardStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private List<TransactionJpaEntity> transactions = new ArrayList<>();

    @Column(name = "family_members", columnDefinition = "TEXT")
    private String familyMembers;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public SubscriptionJpaEntity() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public BigDecimal getPlanPrice() { return planPrice; }
    public void setPlanPrice(BigDecimal planPrice) { this.planPrice = planPrice; }

    public int getPlanDurationAmount() { return planDurationAmount; }
    public void setPlanDurationAmount(int planDurationAmount) { this.planDurationAmount = planDurationAmount; }

    public String getPlanDurationUnit() { return planDurationUnit; }
    public void setPlanDurationUnit(String planDurationUnit) { this.planDurationUnit = planDurationUnit; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCardLastFour() { return cardLastFour; }
    public void setCardLastFour(String cardLastFour) { this.cardLastFour = cardLastFour; }

    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getCardExpiration() { return cardExpiration; }
    public void setCardExpiration(String cardExpiration) { this.cardExpiration = cardExpiration; }

    public String getCardBrand() { return cardBrand; }
    public void setCardBrand(String cardBrand) { this.cardBrand = cardBrand; }

    public String getCardStatus() { return cardStatus; }
    public void setCardStatus(String cardStatus) { this.cardStatus = cardStatus; }

    public List<TransactionJpaEntity> getTransactions() { return transactions; }
    public void setTransactions(List<TransactionJpaEntity> transactions) { this.transactions = transactions; }

    public String getFamilyMembers() { return familyMembers; }
    public void setFamilyMembers(String familyMembers) { this.familyMembers = familyMembers; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(LocalDateTime cancelledAt) { this.cancelledAt = cancelledAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}