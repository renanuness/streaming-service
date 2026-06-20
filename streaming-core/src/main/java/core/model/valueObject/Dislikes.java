package core.model.valueObject;

import core.model.entity.Music;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Dislikes {
    private HashSet<ArtistId> dislikedArtists;
    private List<MusicId> dislikedSongs;

    public void addArtist(ArtistId artistId) {
        dislikedArtists.add(artistId);
    }

    public void removeArtist(ArtistId artistId) {
        dislikedArtists.remove(artistId);
    }

    public boolean isArtistDisliked(ArtistId artistId) {
        return dislikedArtists.contains(artistId);
    }

    public List<MusicId> filterDislikedArtists(List<Music> songs) {
        return songs.stream()
                .filter(song -> !dislikedArtists.contains(song.artist().id()))
                .map(Music::id)
                .collect(Collectors.toList());
    }
}
