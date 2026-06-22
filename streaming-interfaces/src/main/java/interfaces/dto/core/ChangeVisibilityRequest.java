package interfaces.dto.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangeVisibilityRequest(
        @NotBlank @Pattern(regexp = "PUBLIC|PRIVATE|SHARED", message = "Visibilidade inválida")
        String visibility,

        @NotBlank String userId
) {}