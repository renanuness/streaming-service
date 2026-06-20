package application.identity.dto;

import java.time.LocalDate;

public record UserResponse(
        String id,
        String fullName,
        String email,
        int age,
        String status
) {}
