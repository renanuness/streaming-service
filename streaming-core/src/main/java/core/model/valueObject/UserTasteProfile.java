package core.model.valueObject;

import core.model.entity.Genre;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserTasteProfile {
    private Map<Genre, Double> genreAffinity;
    private Map<ArtistId, Double> artistAffinity;
    private List<MusicAttribute> preferredAttributes;
    private LocalDateTime lastUpdated;

    public void updateFromListening(MusicId musicId) {

    }

    public void updateFromDislike(ArtistId artistId) {

    }

    public void updateFromUndislike(ArtistId artistId){

    }

    public List<Genre> suggestGenres() {
        return new ArrayList<>();
    }
}
