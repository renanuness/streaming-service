package core.model.valueObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ListeningHistory {
    private static final int MAX_HISTORY = 500;

    private List<ListeningEntry> entries;

    public void add(MusicId musicId) {
        entries.add(0, new ListeningEntry(musicId, LocalDateTime.now()));

        if (entries.size() > MAX_HISTORY) {
            entries = entries.subList(0, MAX_HISTORY);
        }
    }

    public List<MusicId> recentlyPlayed(int limit) {
        return entries.stream()
                .limit(limit)
                .map(ListeningEntry::musicId)
                .collect(Collectors.toList());
    }
}
