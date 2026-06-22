package subscription.model.valueObject;

import shared.exception.BusinessRuleException;
import java.util.UUID;

public record TransactionId(String value) {

    public TransactionId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID da transação não pode ser vazio");
        }
    }

    public static TransactionId generate() {
        return new TransactionId(UUID.randomUUID().toString());
    }
}


