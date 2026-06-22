package subscription.service;


import subscription.model.valueObject.CreditCard;
import subscription.model.valueObject.Money;
import subscription.model.valueObject.PaymentResult;
import subscription.model.valueObject.TransactionId;

public interface PaymentGateway {

    PaymentResult charge(CreditCard creditCard, Money amount, String description);

    PaymentResult refund(TransactionId transactionId, Money amount);

    boolean validateCard(CreditCard creditCard);

    String tokenizeCard(CreditCard creditCard);
}