package core.event;

import core.model.valueObject.MusicId;
import core.model.valueObject.PlaylistId;
import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;

public class MusicRemovedFromPlaylistEvent extends DomainEvent {
    private PlaylistId playlistId;
    private UserId ownerId;
    private MusicId musicId;

    public MusicRemovedFromPlaylistEvent(PlaylistId playlistId, UserId ownerId, MusicId musicId) {
        this.playlistId= playlistId;
        this.ownerId= ownerId;
        this.musicId= musicId;
    }

    @Override
    public String eventType() {
        return "playlist.music.removed";
    }
}
