package core.event;

import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;

public class PlaylistCreatedEvent extends DomainEvent {
    private PlaylistId id;
    private PlaylistName name;
    private UserId ownerId;

    public PlaylistCreatedEvent(PlaylistId id, PlaylistName name, UserId ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    @Override
    public String eventType() {
        return "playlist.created";
    }
}
