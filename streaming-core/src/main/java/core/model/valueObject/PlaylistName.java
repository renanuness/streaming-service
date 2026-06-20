package core.model.valueObject;

import shared.exception.BusinessRuleException;

public record PlaylistName(String value) {
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    public PlaylistName{
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("Nome da playlist não pode ser vazio");
        }

        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new BusinessRuleException(
                    String.format("Nome da playlist deve ter entre %d e %d caracteres",
                            MIN_LENGTH, MAX_LENGTH)
            );
        }
    }
}
