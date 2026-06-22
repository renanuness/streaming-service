package application.core.service;


import application.core.dto.RecordListeningRequest;
import application.core.dto.UserStatsResponse;
import core.model.aggregate.User;
import core.model.valueObject.ArtistId;
import core.model.valueObject.MusicId;
import core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shared.domain.DomainEventPublisher;
import shared.exception.ResourceNotFoundException;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import shared.model.valueObject.UserId;

import java.util.List;
import java.util.stream.Collectors;

@Service("coreUserApplicationService")
@Transactional
public class UserApplicationService {
    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;

    public UserApplicationService(@Qualifier("coreJpaUserRepository")UserRepository userRepository, DomainEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public void createUserProfile(String userId, String email, String name, String lastName) {
        UserId id = new UserId(userId);

        if (userRepository.existsById(id)) {
            return;
        }

        User user = new User(id, new FullName(name, lastName), new Email(email));

        userRepository.save(user);
        user.publishEvents(eventPublisher);
    }

    public void recordListening(RecordListeningRequest request) {
        UserId id = new UserId(request.userId());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + request.userId()));

        MusicId musicIdVO = new MusicId(request.musicId());
        user.recordListening(musicIdVO);

        userRepository.save(user);

        user.publishEvents(eventPublisher);
    }

    public void deleteUserProfile(String userId) {
        UserId id = new UserId(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        userRepository.delete(user);
    }

    public void favoriteMusic(String userId, String musicId) {
        UserId id = new UserId(userId);
        MusicId musicIdVO = new MusicId(musicId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.favoriteMusic(musicIdVO);

        userRepository.save(user);
        user.publishEvents(eventPublisher);
    }

    public void unfavoriteMusic(String userId, String musicId) {
        UserId id = new UserId(userId);
        MusicId musicIdVO = new MusicId(musicId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.unfavoriteMusic(musicIdVO);

        userRepository.save(user);
        user.publishEvents(eventPublisher);
    }

    @Transactional(readOnly = true)
    public boolean isMusicFavorited(String userId, String musicId) {
        UserId id = new UserId(userId);
        MusicId musicIdVO = new MusicId(musicId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return user.isMusicFavorited(musicIdVO);
    }

    @Transactional(readOnly = true)
    public List<String> getFavoriteMusics(String userId) {
        UserId id = new UserId(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return user.favoriteSongs().stream()
                .map(MusicId::value)
                .collect(Collectors.toList());
    }

    public void dislikeArtist(String userId, String artistId) {
        UserId id = new UserId(userId);
        ArtistId artistIdVO = new ArtistId(artistId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.dislikeArtist(artistIdVO);

        userRepository.save(user);
        user.publishEvents(eventPublisher);
    }

    public void undislikeArtist(String userId, String artistId) {
        UserId id = new UserId(userId);
        ArtistId artistIdVO = new ArtistId(artistId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.undislikeArtist(artistIdVO);

        userRepository.save(user);
        user.publishEvents(eventPublisher);
    }

    @Transactional(readOnly = true)
    public boolean isArtistDisliked(String userId, String artistId) {
        UserId id = new UserId(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return user.isArtistDisliked(new ArtistId(artistId));
    }

    @Transactional(readOnly = true)
    public List<String> getDislikedArtists(String userId) {
        UserId id = new UserId(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return user.dislikedArtists().stream()
                .map(ArtistId::value)
                .collect(Collectors.toList());
    }

    public void recordListening(String userId, String musicId, String source) {
        UserId id = new UserId(userId);
        MusicId musicIdVO = new MusicId(musicId);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.recordListening(musicIdVO);

        userRepository.save(user);
        user.publishEvents(eventPublisher);
    }

    @Transactional(readOnly = true)
    public List<String> getRecentlyPlayed(String userId, int limit) {
        UserId id = new UserId(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return user.recentlyPlayedMusicIds(limit).stream()
                .map(MusicId::value)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserStatsResponse getStats(String userId) {
        UserId id = new UserId(userId);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return new UserStatsResponse(
                user.favoriteCount(),
                user.dislikedArtistsCount(),
                user.playlistCount()
        );
    }
}