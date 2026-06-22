package application.subscription.dto;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        String id,
        String userId,
        String planType,
        String price,
        String status,
        String maskedCard,
        int familyMemberCount,
        LocalDateTime startedAt,
        LocalDateTime expiresAt,
        LocalDateTime cancelledAt
) {}