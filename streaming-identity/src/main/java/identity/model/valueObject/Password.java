package identity.model.valueObject;

import shared.exception.BusinessRuleException;

public record Password(String value) {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;

    public Password {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("Senha não pode ser vazia");
        }
        if (value.length() < MIN_LENGTH) {
            throw new BusinessRuleException(
                    String.format("Senha deve ter no mínimo %d caracteres", MIN_LENGTH)
            );
        }
        if (value.length() > MAX_LENGTH) {
            throw new BusinessRuleException("Senha excede o tamanho máximo permitido");
        }
        if (!possuiLetraMaiuscula(value)) {
            throw new BusinessRuleException("Senha deve conter pelo menos uma letra maiúscula");
        }
        if (!possuiLetraMinuscula(value)) {
            throw new BusinessRuleException("Senha deve conter pelo menos uma letra minúscula");
        }
        if (!possuiNumero(value)) {
            throw new BusinessRuleException("Senha deve conter pelo menos um número");
        }
    }

    private boolean possuiLetraMaiuscula(String senha) {
        return senha.chars().anyMatch(Character::isUpperCase);
    }

    private boolean possuiLetraMinuscula(String senha) {
        return senha.chars().anyMatch(Character::isLowerCase);
    }

    private boolean possuiNumero(String senha) {
        return senha.chars().anyMatch(Character::isDigit);
    }

    public boolean matches(String rawPassword) {
        return this.value.equals(rawPassword);
    }

    @Override
    public String toString() {
        return "Password{value='***'}";
    }
}