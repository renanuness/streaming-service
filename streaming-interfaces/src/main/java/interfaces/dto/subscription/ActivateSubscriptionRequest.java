package interfaces.dto.subscription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ActivateSubscriptionRequest(
        @NotBlank @Size(min = 4, max = 4, message = "Últimos 4 dígitos do cartão")
        String lastFourDigits,

        @NotBlank(message = "Nome do titular é obrigatório")
        String holderName,

        @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}", message = "Formato: YYYY-MM")
        String expirationDate,

        @NotBlank(message = "Bandeira do cartão é obrigatória")
        @Pattern(regexp = "VISA|MASTERCARD|ELO|AMERICAN_EXPRESS|HIPERCARD")
        String brand
) {}
