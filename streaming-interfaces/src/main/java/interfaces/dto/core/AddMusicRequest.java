package interfaces.dto.core;

import jakarta.validation.constraints.NotBlank;

public record AddMusicRequest(
        @NotBlank String musicId,
        @NotBlank String userId,
        boolean isFreePlan
) {}