package core.model.aggregate;

import core.event.*;
import core.model.valueObject.*;
import shared.domain.AggregateRoot;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import shared.model.valueObject.UserId;

import java.util.List;

public class User extends AggregateRoot {
    private final int MAX_FREE_PLAYLISTS = 50;
    private final int MAX_PREMIUM_PLAYLISTS = 1000;


    private final UserId id;
    private FullName name;
    private Email email;
    private UserTasteProfile tasteProfile;
    private ListeningHistory listeningHistory;
    private Favorites favorites;
    private Dislikes dislikes;
    private List<Playlist> playlists;

    public User(FullName name, Email email){
        super();
        id = UserId.generate();
    }

    public void favoriteMusic(MusicId musicId) {
        favorites.add(musicId);
        registerEvent(new MusicFavoritedEvent(this.id, musicId));
    }

    public void unfavoriteMusic(MusicId musicId) {
        favorites.remove(musicId);
        registerEvent(new MusicUnfavoritedEvent(this.id, musicId));
    }

    public void dislikeArtist(ArtistId artistId) {
        dislikes.addArtist(artistId);
        tasteProfile.updateFromDislike(artistId);
        registerEvent(new ArtistDislikedEvent(this.id, artistId));
    }

    public void undislikeArtist(ArtistId artistId) {
        dislikes.removeArtist(artistId);
        tasteProfile.updateFromUndislike(artistId);
        registerEvent(new ArtistUndislikedEvent(this.id, artistId));
    }

    public void recordListening(MusicId musicId) {
        listeningHistory.add(musicId);
        tasteProfile.updateFromListening(musicId);
        registerEvent(new MusicPlayedEvent(this.id, musicId));
    }

    public Playlist createPlaylist(PlaylistName name) {
        Playlist playlist = new Playlist(name, this.id);
        playlists.add(playlist);
        return playlist;
    }

    public boolean canCreatePlaylist(boolean isPremium) {
        int maxPlaylists = isPremium ? MAX_PREMIUM_PLAYLISTS : MAX_FREE_PLAYLISTS;
        return playlists.size() < maxPlaylists;
    }
}
