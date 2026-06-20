package core.model.valueObject;

import java.time.LocalDateTime;

public record ListeningEntry(MusicId musicId, LocalDateTime playedAt) {}

