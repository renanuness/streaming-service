package infra.core.persistence;

import core.model.aggregate.Playlist;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import core.repository.PlaylistRepository;
import org.springframework.stereotype.Component;
import shared.model.valueObject.UserId;

import java.util.List;
import java.util.Optional;

@Component
public class JpaPlaylistRepository implements PlaylistRepository {
    private final PlaylistJpaRepository repository;

    public JpaPlaylistRepository(PlaylistJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Playlist playlist) {
    }

    @Override
    public void delete(Playlist playlist) {

    }

    @Override
    public Optional<Playlist> findById(PlaylistId id) {
        return Optional.empty();
    }

    @Override
    public List<Playlist> findUserPlaylists(UserId id) {
        return List.of();
    }

    @Override
    public List<Playlist> findByName(PlaylistName name) {
        return List.of();
    }
}
