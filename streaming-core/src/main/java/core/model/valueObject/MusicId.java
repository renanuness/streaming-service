package core.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.UUID;

public record MusicId(String value) {

    public MusicId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID da música não pode ser vazio");
        }
    }

    public static MusicId generate() {
        return new MusicId(UUID.randomUUID().toString());
    }
}