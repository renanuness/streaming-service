package interfaces.dto.subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateSubscriptionRequest(
        @NotBlank(message = "ID do usuário é obrigatório")
        String userId,

        @NotBlank(message = "Tipo de plano é obrigatório")
        @Pattern(regexp = "BASIC|PREMIUM|FAMILIAR", message = "Plano deve ser BASIC, PREMIUM ou FAMILIAR")
        String planType
) {}



