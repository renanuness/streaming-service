package core.model.valueObject;

import shared.exception.BusinessRuleException;

import java.util.List;

public class Favorites {
    private static final int MAX_FAVORITES = 10000;

    private List<MusicId> favoriteSongs;
    private List<AlbumId> favoriteAlbums;
    private List<ArtistId> favoriteArtists;

    public void add(MusicId musicId) {
        if (favoriteSongs.size() >= MAX_FAVORITES) {
            throw new BusinessRuleException("Limite máximo de favoritos atingido");
        }
        if (!favoriteSongs.contains(musicId)) {
            favoriteSongs.add(musicId);
        }
    }

    public void remove(MusicId musicId) {
        favoriteSongs.remove(musicId);
    }

    public boolean contains(MusicId musicId) {
        return favoriteSongs.contains(musicId);
    }

    public int count() {
        return favoriteSongs.size();
    }
}
