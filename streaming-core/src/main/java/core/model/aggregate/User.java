package core.model.aggregate;

import core.event.*;
import core.model.valueObject.*;
import shared.domain.AggregateRoot;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import shared.model.valueObject.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User extends AggregateRoot {
    private final int MAX_FREE_PLAYLISTS = 50;
    private final int MAX_PREMIUM_PLAYLISTS = 1000;


    private final UserId id;
    private FullName name;
    private Email email;
    private List<Playlist> playlists;
    private List<MusicId> favoriteSongs;
    private List<ArtistId> dislikedArtists;
    private List<ListeningEntry> recentListening;

    public User(UserId id, FullName name, Email email){
        super();
        this.id = id;
        favoriteSongs = new ArrayList<>();
        dislikedArtists = new ArrayList<>();
        recentListening = new ArrayList<>();
        setCreatedAt(LocalDateTime.now());
        setUpdatedAt(LocalDateTime.now());
    }

    private User(UserId id, FullName name, Email email, List<MusicId> favoriteSongs, List<ArtistId> dislikedArtists, List<ListeningEntry> recentListening, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.favoriteSongs = favoriteSongs;
        this.dislikedArtists = dislikedArtists;
        this.recentListening = recentListening;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public static User reconstruct(
            UserId id,
            FullName name,
            Email email,
            List<MusicId> favoriteSongs,
            List<ArtistId> dislikedArtists,
            List<ListeningEntry> recentListening,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ){
        return new User(id,name,email,favoriteSongs,dislikedArtists,recentListening,createdAt,updatedAt);
    }

    public void favoriteMusic(MusicId musicId) {
        favoriteSongs.add(musicId);
        registerEvent(new MusicFavoritedEvent(this.id, musicId));
    }

    public void unfavoriteMusic(MusicId musicId) {
        favoriteSongs.remove(musicId);
        registerEvent(new MusicUnfavoritedEvent(this.id, musicId));
    }

    public void dislikeArtist(ArtistId artistId) {
        dislikedArtists.add(artistId);
        registerEvent(new ArtistDislikedEvent(this.id, artistId));
    }

    public void undislikeArtist(ArtistId artistId) {
        dislikedArtists.removeIf(a -> a == artistId);
        registerEvent(new ArtistUndislikedEvent(this.id, artistId));
    }

    public boolean isArtistDisliked(ArtistId artistId) {
        return dislikedArtists.stream().anyMatch(a -> a.value() == artistId.value());
    }

    public void recordListening(MusicId musicId) {
        recentListening.add(new ListeningEntry(musicId, LocalDateTime.now()));
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

    public List<MusicId> recentlyPlayedMusicIds(int limit) {
        return recentListening.subList(0, limit).stream().map(i -> i.musicId()).toList();
    }

    public final UserId id(){ return id; }
    public FullName name(){ return name; }
    public Email email(){ return email; }
    public List<Playlist> playlists(){ return playlists; }
    public List<MusicId> favoriteSongs(){ return favoriteSongs; }
    public List<ArtistId> dislikedArtists(){ return dislikedArtists; }
    public List<ListeningEntry> recentListening(){ return recentListening; }


    public int favoriteCount() { return favoriteSongs.size(); }

    public int dislikedArtistsCount() { return dislikedArtists.size(); }

    public int playlistCount() { return playlists.size(); }

    public boolean isMusicFavorited(MusicId musicId) {
        return favoriteSongs.stream().anyMatch(m -> m.value() == musicId.value());
    }
}
