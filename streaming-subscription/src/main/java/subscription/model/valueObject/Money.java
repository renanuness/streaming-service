package subscription.model.valueObject;

import shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public record Money(BigDecimal amount, Currency currency) {

    public Money {
        if (amount == null) {
            throw new BusinessRuleException("Valor não pode ser nulo");
        }
        if (currency == null) {
            throw new BusinessRuleException("Moeda não pode ser nula");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("Valor não pode ser negativo");
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money brl(double amount) {
        return new Money(BigDecimal.valueOf(amount), Currency.getInstance("BRL"));
    }

    public static Money usd(double amount) {
        return new Money(BigDecimal.valueOf(amount), Currency.getInstance("USD"));
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money multiply(int factor) {
        return new Money(this.amount.multiply(BigDecimal.valueOf(factor)), this.currency);
    }

    public boolean isGreaterThan(Money other) {
        validateSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new BusinessRuleException("Moedas diferentes não podem ser comparadas");
        }
    }

    @Override
    public String toString() {
        return currency.getSymbol() + " " + amount;
    }
}
