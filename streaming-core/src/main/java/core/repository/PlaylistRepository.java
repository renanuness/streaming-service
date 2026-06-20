package core.repository;

import core.model.aggregate.Playlist;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import shared.model.valueObject.UserId;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository {
    void save(Playlist playlist);
    void delete(Playlist playlist);
    Optional<Playlist> findById(PlaylistId id);
    List<Playlist> findUserPlaylists(UserId id);
    List<Playlist> findByName(PlaylistName name);
}

