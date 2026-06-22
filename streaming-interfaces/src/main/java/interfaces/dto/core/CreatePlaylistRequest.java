package interfaces.dto.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePlaylistRequest(
        @NotBlank(message = "Nome da playlist é obrigatório")
        @Size(min = 1, max = 100, message = "Nome deve ter entre 1 e 100 caracteres")
        String name,

        @NotBlank(message = "ID do usuário é obrigatório")
        String ownerId,

        boolean isPremium
) {}