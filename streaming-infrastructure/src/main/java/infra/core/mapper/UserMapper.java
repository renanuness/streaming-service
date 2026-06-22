package infra.core.mapper;

import core.model.aggregate.User;
import core.model.valueObject.ArtistId;
import core.model.valueObject.ListeningEntry;
import core.model.valueObject.MusicId;
import infra.core.entity.UserJpaEntity;
import org.springframework.stereotype.Component;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import shared.model.valueObject.UserId;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component("coreUserMapper")
public class UserMapper {

    public User toDomainUser(UserJpaEntity entity){
        var id = new UserId(entity.getId());
        var name = new FullName(entity.getFirstName(), entity.getLastName());
        var email = new Email(entity.getEmail());
        var dislikedArtists = Arrays.stream(entity.getDislikedArtists().split(",")).map(a -> new ArtistId(a)).toList();
        var favoriteSongs = Arrays.stream(entity.getFavoriteSongs().split(",")).map(f -> new MusicId((f))).toList();
        var recentListening = new ObjectMapper().readValue(entity.getRecentListening(),  new TypeReference<List<ListeningEntry>>(){});
        return User.reconstruct(id, name, email, favoriteSongs, dislikedArtists, recentListening, entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public UserJpaEntity toJpaEntity(User user){
        var entity = new UserJpaEntity();
        entity.setId(user.id().value());
        entity.setFirstName(user.name().firstName());
        entity.setLastName(user.name().lastName());
        entity.setEmail(user.email().value());
        entity.setFavoriteSongs(user.favoriteSongs().stream().map(m -> m.value()).collect(Collectors.joining(",")));
        entity.setDislikedArtists(user.dislikedArtists().stream().map(d -> d.value()).collect(Collectors.joining(",")));
        entity.setRecentListening(new ObjectMapper().writeValueAsString(user.recentListening()));
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());

        return entity;
    }
}

