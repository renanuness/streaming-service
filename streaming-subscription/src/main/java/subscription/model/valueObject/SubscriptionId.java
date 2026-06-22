package subscription.model.valueObject;

import shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.UUID;

public record SubscriptionId(String value) {

    public SubscriptionId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID da assinatura não pode ser vazio");
        }
    }

    public static SubscriptionId generate() {
        return new SubscriptionId(UUID.randomUUID().toString());
    }
}