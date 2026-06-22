package core.event;

import core.model.valueObject.PlaylistId;
import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;

public class PlaylistReorderedEvent extends DomainEvent {
    private PlaylistId id;
    private UserId ownerId;

    public PlaylistReorderedEvent(PlaylistId id, UserId ownerId) {
        this.id = id;
        this.ownerId = ownerId;
    }

    @Override
    public String eventType() {
        return "playlist.reordered";
    }
}
