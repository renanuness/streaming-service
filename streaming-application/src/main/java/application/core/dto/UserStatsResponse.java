package application.core.dto;

public record UserStatsResponse(
        int favoriteCount,
        int dislikedArtistsCount,
        int playlistCount
) {}
