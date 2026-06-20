package core.event;

import core.model.valueObject.MusicId;
import shared.domain.DomainEvent;
import shared.model.valueObject.UserId;

public class MusicFavoritedEvent extends DomainEvent {
    private UserId userId;
    private MusicId musicId;

    public MusicFavoritedEvent(UserId userId, MusicId musicId){
        this.userId = userId;
        this.musicId = musicId;
    }
    @Override
    public String eventType() {

        return "music.favorited";
    }
}
