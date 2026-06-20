package core.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.UUID;

public record PlaylistId(String value) {
    public PlaylistId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID da playlist não pode ser vazio");
        }
    }

    public static PlaylistId generate(){
        return new PlaylistId(UUID.randomUUID().toString());
    }
}
