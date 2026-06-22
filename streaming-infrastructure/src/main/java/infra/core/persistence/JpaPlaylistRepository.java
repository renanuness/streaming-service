package infra.core.persistence;

import core.model.aggregate.Playlist;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import core.repository.PlaylistRepository;
import infra.core.mapper.PlaylistMapper;
import org.springframework.stereotype.Component;
import shared.model.valueObject.UserId;

import java.util.List;
import java.util.Optional;

@Component
public class JpaPlaylistRepository implements PlaylistRepository {
    private final PlaylistJpaRepository repository;
    private final PlaylistMapper mapper;

    public JpaPlaylistRepository(PlaylistJpaRepository repository, PlaylistMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Playlist playlist) {
        repository.save(mapper.toJpaEntity(playlist));
    }

    @Override
    public void delete(Playlist playlist) {

    }

    @Override
    public Optional<Playlist> findById(PlaylistId id) {
        return Optional.empty();
    }

    @Override
    public List<Playlist> findByOwner(UserId id) {
        return List.of();
    }

    @Override
    public List<Playlist> findByName(PlaylistName name) {
        return List.of();
    }


    @Override
    public boolean existsByNameAndOwner(PlaylistName name, UserId ownerId) {
        return repository.existsByNameAndOwnerId(name.value(),ownerId.value());
    }

    @Override
    public int countByOwner(UserId ownerId) {
        return repository.countByOwner(ownerId.value());
    }
}
