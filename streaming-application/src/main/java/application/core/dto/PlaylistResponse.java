package application.core.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PlaylistResponse(
        String id,
        String name,
        String ownerId,
        List<MusicResponse> musics,
        String visibility,
        int musicCount,
        String totalDuration,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}