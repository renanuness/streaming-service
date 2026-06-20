package shared.model.valueObject;


import shared.exception.BusinessRuleException;

public record FullName(String firstName, String lastName) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;

    public FullName {
        if (firstName == null || firstName.isBlank()) {
            throw new BusinessRuleException("Nome não pode ser vazio");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new BusinessRuleException("Sobrenome não pode ser vazio");
        }
        if (firstName.length() < MIN_LENGTH || firstName.length() > MAX_LENGTH) {
            throw new BusinessRuleException(
                    String.format("Nome deve ter entre %d e %d caracteres", MIN_LENGTH, MAX_LENGTH)
            );
        }
        if (lastName.length() < MIN_LENGTH || lastName.length() > MAX_LENGTH) {
            throw new BusinessRuleException(
                    String.format("Sobrenome deve ter entre %d e %d caracteres", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

    public String fullName() {
        return String.format("%s %s", firstName, lastName);
    }
}