package subscription.model.entity;

import subscription.model.valueObject.Money;
import subscription.model.valueObject.TransactionId;
import subscription.model.valueObject.TransactionStatus;
import subscription.model.valueObject.TransactionType;
import shared.exception.BusinessRuleException;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {

    private final TransactionId id;
    private final Money amount;
    private final String merchant;
    private final TransactionType type;
    private TransactionStatus status;
    private final LocalDateTime timestamp;
    private String failureReason;

    public Transaction(Money amount, String merchant, TransactionType type) {
        this.id = TransactionId.generate();
        this.amount = amount;
        this.merchant = merchant;
        this.type = type;
        this.status = TransactionStatus.PENDING;
        this.timestamp = LocalDateTime.now();
        this.failureReason = null;

        validate();
    }

    private Transaction(
            TransactionId id,
            Money amount,
            String merchant,
            TransactionType type,
            TransactionStatus status,
            LocalDateTime timestamp,
            String failureReason
    ) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.type = type;
        this.status = status;
        this.timestamp = timestamp;
        this.failureReason = failureReason;
    }

    public static Transaction reconstruct(
            TransactionId id,
            Money amount,
            String merchant,
            TransactionType type,
            TransactionStatus status,
            LocalDateTime timestamp,
            String failureReason
    ) {
        return new Transaction(id, amount, merchant, type, status, timestamp, failureReason);
    }

    public void approve() {
        if (this.status != TransactionStatus.PENDING) {
            throw new BusinessRuleException(
                    "Apenas transações pendentes podem ser aprovadas. Status atual: " + this.status
            );
        }

        this.status = TransactionStatus.APPROVED;
    }

    public void decline(String reason) {
        if (this.status != TransactionStatus.PENDING) {
            throw new BusinessRuleException(
                    "Apenas transações pendentes podem ser recusadas"
            );
        }
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleException("Motivo da recusa é obrigatório");
        }

        this.status = TransactionStatus.DECLINED;
        this.failureReason = reason;
    }

    public void fail(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new BusinessRuleException("Motivo da falha é obrigatório");
        }

        this.status = TransactionStatus.FAILED;
        this.failureReason = reason;
    }

    public void refund() {
        if (this.status != TransactionStatus.APPROVED) {
            throw new BusinessRuleException(
                    "Apenas transações aprovadas podem ser estornadas"
            );
        }

        this.status = TransactionStatus.REFUNDED;
    }

    public void cancel() {
        if (this.status == TransactionStatus.APPROVED ||
                this.status == TransactionStatus.REFUNDED) {
            throw new BusinessRuleException(
                    "Transações aprovadas ou estornadas não podem ser canceladas"
            );
        }

        this.status = TransactionStatus.CANCELLED;
    }

    private void validate() {
        if (amount == null) {
            throw new BusinessRuleException("Valor da transação não pode ser nulo");
        }
        if (merchant == null || merchant.isBlank()) {
            throw new BusinessRuleException("Comerciante não pode ser vazio");
        }
        if (type == null) {
            throw new BusinessRuleException("Tipo da transação não pode ser nulo");
        }
    }

    public boolean isApproved() {
        return status == TransactionStatus.APPROVED;
    }

    public boolean isDeclined() {
        return status == TransactionStatus.DECLINED;
    }

    public boolean isSimilarTo(Transaction other) {
        return this.amount.equals(other.amount) &&
                this.merchant.equals(other.merchant);
    }

    public boolean occurredInLastMinutes(int minutes) {
        LocalDateTime limit = LocalDateTime.now().minusMinutes(minutes);
        return this.timestamp.isAfter(limit);
    }

    public TransactionId id() { return id; }
    public Money amount() { return amount; }
    public String merchant() { return merchant; }
    public TransactionType type() { return type; }
    public TransactionStatus status() { return status; }
    public LocalDateTime timestamp() { return timestamp; }
    public String failureReason() { return failureReason; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction[id=%s, amount=%s, merchant=%s, status=%s, timestamp=%s]",
                id.value(), amount, merchant, status, timestamp
        );
    }
}