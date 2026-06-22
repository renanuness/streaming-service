
package subscription.model.valueObject;

import shared.exception.BusinessRuleException;
import java.time.YearMonth;

public record CreditCard(
        String lastFourDigits,
        String holderName,
        YearMonth expirationDate,
        CardBrand brand,
        CardStatus status
) {

    public CreditCard {
        if (lastFourDigits == null || lastFourDigits.length() != 4) {
            throw new BusinessRuleException("Últimos 4 dígitos do cartão inválidos");
        }
        if (holderName == null || holderName.isBlank()) {
            throw new BusinessRuleException("Nome do titular é obrigatório");
        }
        if (expirationDate == null) {
            throw new BusinessRuleException("Data de expiração é obrigatória");
        }
        if (brand == null) {
            throw new BusinessRuleException("Bandeira do cartão é obrigatória");
        }
        if (status == null) {
            throw new BusinessRuleException("Status do cartão é obrigatório");
        }
    }

    public boolean isExpired() {
        return expirationDate.isBefore(YearMonth.now());
    }

    public boolean canProcessPayment() {
        return status == CardStatus.ACTIVE && !isExpired();
    }

    public String maskedNumber() {
        return "****-****-****-" + lastFourDigits;
    }

    public static CreditCard createActive(String lastFourDigits, String holderName,
                                          YearMonth expiration, CardBrand brand) {
        return new CreditCard(lastFourDigits, holderName, expiration, brand, CardStatus.ACTIVE);
    }
}