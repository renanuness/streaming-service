package application.subscription.dto;

import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        String amount,
        String merchant,
        String type,
        String status,
        LocalDateTime timestamp,
        String failureReason
) {}