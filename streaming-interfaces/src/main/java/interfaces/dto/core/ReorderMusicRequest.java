package interfaces.dto.core;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReorderMusicRequest(
        @Min(0) int newPosition,
        @NotBlank String userId
) {}