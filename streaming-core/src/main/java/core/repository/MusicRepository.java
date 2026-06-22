package core.repository;

import core.model.entity.Music;
import core.model.valueObject.MusicId;

import java.util.List;
import java.util.Optional;

public interface MusicRepository {
    Optional<Music> findById(MusicId musicId);
    List<Music> searchByTitle(String query);
}
