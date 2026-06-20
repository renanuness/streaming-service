package identity.model.valueObject;

import shared.exception.BusinessRuleException;

import java.time.LocalDate;
import java.time.Period;

public record DateOfBirth(LocalDate value) {

    private static final int IDADE_MINIMA = 13;
    private static final int IDADE_MAXIMA = 120;

    public DateOfBirth {
        if (value == null) {
            throw new BusinessRuleException("Data de nascimento não pode ser nula");
        }
        if (value.isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Data de nascimento não pode ser no futuro");
        }

        int idade = Period.between(value, LocalDate.now()).getYears();

        if (idade < IDADE_MINIMA) {
            throw new BusinessRuleException(
                    String.format("Usuário deve ter pelo menos %d anos", IDADE_MINIMA)
            );
        }
        if (idade > IDADE_MAXIMA) {
            throw new BusinessRuleException("Data de nascimento inválida");
        }
    }

    public int age() {
        return Period.between(value, LocalDate.now()).getYears();
    }
}