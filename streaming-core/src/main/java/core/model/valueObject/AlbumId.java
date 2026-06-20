package core.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.UUID;

public record AlbumId(String value){

    public AlbumId {
        if (value == null || value.isBlank()) {
            throw new BusinessRuleException("ID do álbum não pode ser vazio");
        }
    }

    public static AlbumId generate() {
        return new AlbumId(UUID.randomUUID().toString());
    }
}