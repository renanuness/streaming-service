package subscription.service;

import subscription.model.entity.Transaction;
import subscription.model.valueObject.CreditCard;
import shared.exception.BusinessRuleException;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionValidationService {

    private static final int MAX_TRANSACTIONS_IN_WINDOW = 3;
    private static final int MAX_SIMILAR_TRANSACTIONS = 2;
    private static final int TIME_WINDOW_MINUTES = 2;

    public void validate(
            CreditCard creditCard,
            List<Transaction> transactionHistory,
            Transaction newTransaction
    ) {
        validateCreditCardActive(creditCard);
        validateCreditCardNotExpired(creditCard);
        validateHighFrequency(transactionHistory);
        validateDuplicateTransaction(transactionHistory, newTransaction);
    }

    private void validateCreditCardActive(CreditCard creditCard) {
        if (!creditCard.canProcessPayment()) {
            throw new BusinessRuleException("cartão não ativo");
        }
    }

    private void validateCreditCardNotExpired(CreditCard creditCard) {
        if (creditCard.isExpired()) {
            throw new BusinessRuleException("Cartão vencido");
        }
    }

    private void validateHighFrequency(List<Transaction> transactions) {
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES);

        long recentTransactions = transactions.stream()
                .filter(t -> t.timestamp().isAfter(windowStart))
                .filter(t -> t.isApproved() || t.status().name().equals("PENDING"))
                .count();

        if (recentTransactions >= MAX_TRANSACTIONS_IN_WINDOW) {
            throw new BusinessRuleException(
                    String.format(
                            "alta-frequência-pequeno-intervalo: Máximo de %d transações em %d minutos",
                            MAX_TRANSACTIONS_IN_WINDOW, TIME_WINDOW_MINUTES
                    )
            );
        }
    }

    private void validateDuplicateTransaction(
            List<Transaction> transactions,
            Transaction newTransaction
    ) {
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES);

        long similarTransactions = transactions.stream()
                .filter(t -> t.timestamp().isAfter(windowStart))
                .filter(t -> t.isSimilarTo(newTransaction))
                .count();

        if (similarTransactions >= MAX_SIMILAR_TRANSACTIONS) {
            throw new BusinessRuleException(
                    "transação duplicada: Máximo de " + MAX_SIMILAR_TRANSACTIONS +
                            " transações com mesmo valor e comerciante em " + TIME_WINDOW_MINUTES + " minutos"
            );
        }
    }
}