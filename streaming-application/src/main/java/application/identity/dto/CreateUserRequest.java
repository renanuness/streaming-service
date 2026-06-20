package application.identity.dto;

import java.time.LocalDate;

    public record CreateUserRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        LocalDate dateOfBirth
) {}