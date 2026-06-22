package interfaces.dto.subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProcessPaymentRequest(
        @Positive(message = "Valor deve ser positivo")
        double amount,

        @NotBlank(message = "Comerciante é obrigatório")
        String merchant
) {}
