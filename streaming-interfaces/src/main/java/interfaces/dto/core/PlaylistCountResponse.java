package interfaces.dto.core;

public record PlaylistCountResponse(
        String userId,
        int count
) {}