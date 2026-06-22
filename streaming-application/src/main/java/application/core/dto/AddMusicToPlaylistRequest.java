package application.core.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AddMusicToPlaylistRequest(
        String playlistId,
        String musicId,
        String userId,
        boolean isFreePlan
) {}
