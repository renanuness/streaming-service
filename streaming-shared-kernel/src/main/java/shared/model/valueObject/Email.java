package shared.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private static final int MAX_LENGTH = 254;

    public Email {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("Email não pode ser vazio");
        }
        if (value.length() > MAX_LENGTH) {
            throw new BusinessRuleException("Email excede o tamanho máximo permitido");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new BusinessRuleException("Formato de email inválido");
        }
    }
}