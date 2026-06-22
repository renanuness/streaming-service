package subscription.service;

import subscription.model.aggregate.Subscription;
import subscription.model.entity.Transaction;
import subscription.model.valueObject.*;
import shared.exception.BusinessRuleException;

public class PaymentDomainService {

    private static final int MAX_TRANSACTIONS_IN_WINDOW = 3;
    private static final int MAX_SIMILAR_TRANSACTIONS = 2;
    private static final int TIME_WINDOW_MINUTES = 2;

    private final TransactionValidationService validationService;

    public PaymentDomainService() {
        this.validationService = new TransactionValidationService();
    }

    public Transaction processPayment(
            Subscription subscription,
            Money amount,
            String merchant
    ) {
        if (!subscription.isActive()) {
            throw new BusinessRuleException("Assinatura não está ativa para processar pagamento");
        }

        validateCreditCard(subscription.creditCard());

        Transaction transaction = new Transaction(amount, merchant, TransactionType.PAYMENT);

        validationService.validate(
                subscription.creditCard(),
                subscription.transactions(),
                transaction
        );

        transaction.approve();

        return transaction;
    }

    public Transaction processPaymentSafely(
            Subscription subscription,
            Money amount,
            String merchant
    ) {
        try {
            Transaction transaction = processPayment(subscription, amount, merchant);
            subscription.processPayment(amount, merchant);
            return transaction;
        } catch (BusinessRuleException e) {
            Transaction failedTransaction = new Transaction(amount, merchant, TransactionType.PAYMENT);

            if (e.getMessage().contains("cartão não ativo")) {
                failedTransaction.decline("Cartão não ativo");
                subscription.suspend("Cartão não ativo");
            } else if (e.getMessage().contains("alta-frequência")) {
                failedTransaction.decline("Alta frequência de transações");
            } else if (e.getMessage().contains("transação duplicada")) {
                failedTransaction.decline("Transação duplicada");
            } else {
                failedTransaction.fail(e.getMessage());
            }

            throw e;
        }
    }

    public Transaction refundPayment(Subscription subscription, TransactionId transactionId) {
        Transaction transaction = subscription.transactions().stream()
                .filter(t -> t.id().equals(transactionId))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Transação não encontrada"));

        transaction.refund();

        return transaction;
    }

    private void validateCreditCard(CreditCard creditCard) {
        if (creditCard == null) {
            throw new BusinessRuleException("Cartão de crédito não cadastrado");
        }

        if (!creditCard.canProcessPayment()) {
            throw new BusinessRuleException("cartão não ativo");
        }

        if (creditCard.isExpired()) {
            throw new BusinessRuleException("Cartão vencido");
        }
    }
}