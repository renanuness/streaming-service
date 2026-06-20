package core.event;

import core.model.valueObject.ArtistId;
import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;

public class ArtistUndislikedEvent extends DomainEvent {
    private UserId userId;
    private ArtistId artistId;

    public ArtistUndislikedEvent(UserId userId, ArtistId artistId) {
        this.userId = userId;
        this.artistId = artistId;
    }

    @Override
    public String eventType() {
        return "artist.undislike";
    }
}
