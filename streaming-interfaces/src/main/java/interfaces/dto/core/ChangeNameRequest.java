package interfaces.dto.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeNameRequest(
        @NotBlank @Size(min = 1, max = 100) String newName,
        @NotBlank String userId
) {}