package interfaces.dto.subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePlanRequest(
        @NotBlank
        @Pattern(regexp = "BASIC|PREMIUM|FAMILIAR")
        String newPlanType,

        boolean chargeDifference
) {}
