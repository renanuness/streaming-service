package shared.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.UUID;

public record UserId(String value) {

    public UserId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID do usuário não pode ser vazio");
        }
        if (value.length() > 100) {
            throw new BusinessRuleException("ID do usuário muito longo");
        }
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }
}
