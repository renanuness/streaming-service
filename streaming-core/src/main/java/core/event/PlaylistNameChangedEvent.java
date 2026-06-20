package core.event;

import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import shared.domain.DomainEvent;

public class PlaylistNameChangedEvent extends DomainEvent {
    private PlaylistId id;
    private PlaylistName oldName;
    private PlaylistName newName;

    public PlaylistNameChangedEvent(PlaylistId id, PlaylistName oldName, PlaylistName newName) {
        this.id = id;
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public String eventType() {
        return "playlist.nameChanged";
    }
}
