package core.model.valueObject;

import shared.exception.BusinessRuleException;

public record MusicAttribute(String attribute) {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public MusicAttribute{
        if(attribute.length() < MIN_LENGTH || attribute.length() > MAX_LENGTH){
            throw new BusinessRuleException(
                    String.format("O atributo deve ter entre %d e %d caracteres")
            );
        }
    }
}
