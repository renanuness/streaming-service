package core.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.UUID;

public record ArtistId(String value) {

    public ArtistId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID do artista não pode ser vazio");
        }
    }

    public static ArtistId generate() {
        return new ArtistId(UUID.randomUUID().toString());
    }
}
