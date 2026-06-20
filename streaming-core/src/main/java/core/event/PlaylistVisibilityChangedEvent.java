package core.event;

import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistVisibility;
import shared.domain.DomainEvent;

public class PlaylistVisibilityChangedEvent extends DomainEvent {
    private PlaylistId id;
    private PlaylistVisibility oldVisibility;
    private PlaylistVisibility newVisibility;

    public PlaylistVisibilityChangedEvent(PlaylistId id, PlaylistVisibility oldVisibility, PlaylistVisibility newVisibility) {
        this.id = id;
        this.newVisibility = this.newVisibility;
        this.oldVisibility = oldVisibility;
    }

    @Override
    public String eventType() {
        return "playlist.visibilityChanged";
    }
}
