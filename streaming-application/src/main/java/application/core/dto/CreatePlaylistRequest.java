package application.core.dto;

public record CreatePlaylistRequest(
        String name,
        String ownerId,
        boolean isPremium
) {}
