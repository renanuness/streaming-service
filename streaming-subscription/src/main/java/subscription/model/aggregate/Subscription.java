package subscription.model.aggregate;
import shared.model.valueObject.UserId;
import subscription.model.entity.Transaction;
import subscription.model.valueObject.*;
import subscription.event.*;
import subscription.service.TransactionValidationService;
import shared.domain.AggregateRoot;
import shared.exception.BusinessRuleException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Subscription extends AggregateRoot {

    private static final int MAX_FAMILY_MEMBERS = 5;

    private final SubscriptionId id;
    private final UserId userId;
    private Plan plan;
    private SubscriptionStatus status;
    private CreditCard creditCard;
    private List<Transaction> transactions;
    private List<UserId> familyMembers;
    private LocalDateTime startedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime cancelledAt;

    public Subscription(UserId userId, Plan plan) {
        super();
        this.id = SubscriptionId.generate();
        this.userId = userId;
        this.plan = plan;
        this.status = SubscriptionStatus.PENDING;
        this.transactions = new ArrayList<>();
        this.familyMembers = new ArrayList<>();
        this.startedAt = null;
        this.expiresAt = null;
        this.cancelledAt = null;
    }

    private Subscription(
            SubscriptionId id,
            UserId userId,
            Plan plan,
            SubscriptionStatus status,
            CreditCard creditCard,
            List<Transaction> transactions,
            List<UserId> familyMembers,
            LocalDateTime startedAt,
            LocalDateTime expiresAt,
            LocalDateTime cancelledAt
    ) {
        super();
        this.id = id;
        this.userId = userId;
        this.plan = plan;
        this.status = status;
        this.creditCard = creditCard;
        this.transactions = new ArrayList<>(transactions);
        this.familyMembers = new ArrayList<>(familyMembers);
        this.startedAt = startedAt;
        this.expiresAt = expiresAt;
        this.cancelledAt = cancelledAt;
    }

    public static Subscription reconstruct(
            SubscriptionId id,
            UserId userId,
            Plan plan,
            SubscriptionStatus status,
            CreditCard creditCard,
            List<Transaction> transactions,
            List<UserId> familyMembers,
            LocalDateTime startedAt,
            LocalDateTime expiresAt,
            LocalDateTime cancelledAt
    ) {
        return new Subscription(
                id, userId, plan, status, creditCard, transactions,
                familyMembers, startedAt, expiresAt, cancelledAt
        );
    }

    public void activate(CreditCard creditCard) {
        if (!status.canBeActivated()) {
            throw new BusinessRuleException(
                    "Assinatura não pode ser ativada. Status atual: " + status.description()
            );
        }

        if (!creditCard.canProcessPayment()) {
            throw new BusinessRuleException("Cartão não está ativo ou está vencido");
        }

        this.creditCard = creditCard;
        this.status = SubscriptionStatus.ACTIVE;
        this.startedAt = LocalDateTime.now();
        this.expiresAt = calculateExpirationDate();

        registerEvent(new SubscriptionActivatedEvent(
                this.id, this.userId, this.plan.type(), this.plan.price()
        ));
    }


    public Transaction processPayment(Money amount, String merchant) {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException(
                    "Assinatura não está ativa para processar pagamento"
            );
        }

        Transaction transaction = new Transaction(amount, merchant, TransactionType.PAYMENT);

        TransactionValidationService validationService = new TransactionValidationService();
        validationService.validate(this.creditCard, this.transactions, transaction);

        transaction.approve();

        this.transactions.add(transaction);

        this.expiresAt = calculateExpirationDate();

        registerEvent(new PaymentProcessedEvent(
                this.id, this.userId, transaction.id(), amount
        ));

        return transaction;
    }

    public Transaction processPaymentWithSuspension(Money amount, String merchant) {
        try {
            return processPayment(amount, merchant);
        } catch (BusinessRuleException e) {
            if (e.getMessage().contains("cartão não ativo") ||
                    e.getMessage().contains("Cartão vencido")) {
                suspend("Falha no pagamento: " + e.getMessage());
            }
            throw e;
        }
    }

    public void changePlan(Plan newPlan) {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException("Só é possível alterar o plano de uma assinatura ativa");
        }

        if (this.plan.type() == newPlan.type()) {
            throw new BusinessRuleException("Novo plano é igual ao plano atual");
        }

        if (this.plan.allowsMultipleMembers() && !newPlan.allowsMultipleMembers()) {
            if (!this.familyMembers.isEmpty()) {
                this.familyMembers.clear();
            }
        }

        Plan oldPlan = this.plan;
        this.plan = newPlan;

        registerEvent(new PlanChangedEvent(this.id, this.userId, oldPlan.type(), newPlan.type()));
    }

    public void addFamilyMember(UserId memberId) {
        if (!this.plan.allowsMultipleMembers()) {
            throw new BusinessRuleException(
                    "Apenas o plano FAMILIAR permite adicionar membros"
            );
        }

        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException("Assinatura não está ativa");
        }

        if (this.userId.equals(memberId)) {
            throw new BusinessRuleException("Titular não pode ser adicionado como membro");
        }

        if (this.familyMembers.contains(memberId)) {
            throw new BusinessRuleException("Este usuário já é membro do plano familiar");
        }

        if (this.familyMembers.size() >= MAX_FAMILY_MEMBERS) {
            throw new BusinessRuleException(
                    "Limite máximo de " + MAX_FAMILY_MEMBERS + " membros atingido"
            );
        }

        this.familyMembers.add(memberId);

        registerEvent(new FamilyMemberAddedEvent(this.id, this.userId, memberId));
    }

    /**
     * Remove um membro do plano familiar.
     */
    public void removeFamilyMember(UserId memberId) {
        if (!this.familyMembers.contains(memberId)) {
            throw new BusinessRuleException("Usuário não é membro deste plano familiar");
        }

        this.familyMembers.remove(memberId);

        registerEvent(new FamilyMemberRemovedEvent(this.id, this.userId, memberId));
    }

    public void suspend(String reason) {
        if (this.status != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException("Apenas assinaturas ativas podem ser suspensas");
        }

        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleException("Motivo da suspensão é obrigatório");
        }

        this.status = SubscriptionStatus.SUSPENDED;

        registerEvent(new SubscriptionSuspendedEvent(this.id, this.userId, reason));
    }

    public void reactivate(CreditCard creditCard) {
        if (this.status != SubscriptionStatus.SUSPENDED) {
            throw new BusinessRuleException("Apenas assinaturas suspensas podem ser reativadas");
        }

        activate(creditCard);

        registerEvent(new SubscriptionReactivatedEvent(this.id, this.userId));
    }

    public void cancel() {
        if (!status.canBeCancelled()) {
            throw new BusinessRuleException(
                    "Assinatura não pode ser cancelada. Status atual: " + status.description()
            );
        }

        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.familyMembers.clear();

        registerEvent(new SubscriptionCancelledEvent(this.id, this.userId));
    }

    public void cancelDueToUserDeletion() {
        if (this.status == SubscriptionStatus.CANCELLED) {
            return; // Já está cancelada
        }

        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        this.familyMembers.clear();

        registerEvent(new SubscriptionCancelledDueToUserDeletionEvent(this.id, this.userId));
    }

    public void renew(CreditCard creditCard) {
        if (this.status == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleException("Assinatura cancelada não pode ser renovada");
        }

        if (this.status == SubscriptionStatus.EXPIRED) {
            this.status = SubscriptionStatus.PENDING;
        }

        activate(creditCard);

        registerEvent(new SubscriptionRenewedEvent(this.id, this.userId, this.plan.type()));
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }

    public boolean canAddMembers() {
        return this.plan.allowsMultipleMembers() &&
                this.isActive() &&
                this.familyMembers.size() < MAX_FAMILY_MEMBERS;
    }

    public boolean isOwner(UserId userId) {
        return this.userId.equals(userId);
    }

    public boolean isFamilyMember(UserId userId) {
        return this.familyMembers.contains(userId);
    }

    public int familyMemberCount() {
        return familyMembers.size();
    }

    public int recentTransactionCount(int minutes) {
        LocalDateTime limit = LocalDateTime.now().minusMinutes(minutes);
        return (int) transactions.stream()
                .filter(t -> t.timestamp().isAfter(limit))
                .count();
    }

    private LocalDateTime calculateExpirationDate() {
        LocalDateTime base = startedAt != null ? startedAt : LocalDateTime.now();

        return switch (this.plan.duration().unit()) {
            case DAY -> base.plusDays(this.plan.duration().amount());
            case MONTH -> base.plusMonths(this.plan.duration().amount());
            case YEAR -> base.plusYears(this.plan.duration().amount());
        };
    }

    public SubscriptionId id() { return id; }
    public UserId userId() { return userId; }
    public Plan plan() { return plan; }
    public SubscriptionStatus status() { return status; }
    public CreditCard creditCard() { return creditCard; }
    public List<Transaction> transactions() {
        return Collections.unmodifiableList(transactions);
    }
    public List<UserId> familyMembers() {
        return Collections.unmodifiableList(familyMembers);
    }
    public LocalDateTime startedAt() { return startedAt; }
    public LocalDateTime expiresAt() { return expiresAt; }
    public LocalDateTime cancelledAt() { return cancelledAt; }
}