package infra.core.mapper;

import core.model.aggregate.Playlist;
import core.model.entity.Music;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import core.model.valueObject.PlaylistVisibility;
import infra.core.entity.PlaylistJpaEntity;
import infra.core.entity.PlaylistVisibilityEntity;
import org.springframework.stereotype.Component;
import shared.model.valueObject.UserId;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
public class PlaylistMapper{
    public PlaylistJpaEntity toJpaEntity(Playlist playlist){
        var entity = new PlaylistJpaEntity();
        entity.setId(playlist.id().value());
        entity.setName(playlist.name().value());
        entity.setStatus(PlaylistVisibilityEntity.valueOf(playlist.visibility().description()));
        entity.setOwnerId(playlist.ownerId().value());
        entity.setMusics(new ObjectMapper().writeValueAsString(playlist.musics()));
        entity.setCreatedAt(playlist.createdAt());
        entity.setUpdatedAt(playlist.updatedAt());

        return entity;
    }

    public Playlist toDomainPlaylist(PlaylistJpaEntity entity){
        var id = new PlaylistId(entity.getId());
        var name = new PlaylistName(entity.getName());
        var visibility = PlaylistVisibility.valueOf(entity.getStatus().description());
        var ownerId = new UserId(entity.getOwnerId());
        var musics = new ObjectMapper().readValue(entity.getMusics(), new TypeReference<List<Music>>(){});

        return Playlist.reconstruct(id, name, ownerId, musics, visibility, entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
