package application.core.dto;

public record MusicResponse(
        String id,
        String title,
        String artist,
        String duration,
        String genre
) {}