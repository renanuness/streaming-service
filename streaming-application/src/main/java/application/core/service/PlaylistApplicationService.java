package application.core.service;

import application.core.dto.AddMusicToPlaylistRequest;
import application.core.dto.CreatePlaylistRequest;
import application.core.dto.MusicResponse;
import application.core.dto.PlaylistResponse;
import core.model.aggregate.Playlist;
import core.model.entity.Music;
import core.model.valueObject.MusicId;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import core.model.valueObject.PlaylistVisibility;
import core.repository.MusicRepository;
import core.repository.PlaylistRepository;
import core.service.PlaylistDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shared.domain.DomainEventPublisher;
import shared.exception.BusinessRuleException;
import shared.exception.ForbiddenException;
import shared.exception.ResourceNotFoundException;
import shared.model.valueObject.UserId;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlaylistApplicationService {
    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;
    private final PlaylistDomainService playlistDomainService;
    private final DomainEventPublisher eventPublisher;

    public PlaylistApplicationService(
            PlaylistRepository playlistRepository, MusicRepository musicRepository,
            PlaylistDomainService playlistDomainService,
            DomainEventPublisher eventPublisher
    ) {
        this.playlistRepository = playlistRepository;
        this.musicRepository = musicRepository;
        this.playlistDomainService = playlistDomainService;
        this.eventPublisher = eventPublisher;
    }

    public PlaylistResponse createPlaylist(String name,
                                           String ownerId,
                                           boolean isPremium) {
        UserId ownerIdVO = new UserId(ownerId);
        PlaylistName playlistName = new PlaylistName(name);

        if (playlistRepository.existsByNameAndOwner(playlistName, ownerIdVO)) {
            throw new BusinessRuleException("Você já tem uma playlist com este nome");
        }

        int count = playlistRepository.countByOwner(ownerIdVO);
        if (count >= 50 && !isPremium) {
            throw new BusinessRuleException(
                    "Limite de 50 playlists atingido. Faça upgrade para o plano premium!"
            );
        }

        Playlist playlist = playlistDomainService.createPlaylist(playlistName, ownerIdVO);
        playlistRepository.save(playlist);
        playlist.publishEvents(eventPublisher);
        return toResponse(playlist);
    }

    @Transactional(readOnly = true)
    public PlaylistResponse findById(String playlistId) {
        PlaylistId id = new PlaylistId(playlistId);
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada: " + playlistId));

        return toResponse(playlist);
    }

    @Transactional(readOnly = true)
    public List<PlaylistResponse> findByOwner(String userId) {
        UserId ownerId = new UserId(userId);

        return playlistRepository.findByOwner(ownerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int countByOwner(String userId) {
        return playlistRepository.countByOwner(new UserId(userId));
    }

    public PlaylistResponse addMusic(AddMusicToPlaylistRequest request) {
        PlaylistId playlistId = new PlaylistId(request.playlistId());
        MusicId musicId = new MusicId(request.musicId());
        UserId userId = new UserId(request.userId());

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada"));

        if (!playlist.isOwnedBy(userId)) {
            throw new ForbiddenException("Apenas o dono pode adicionar músicas");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Música não encontrada"));

        playlist.addMusic(music, request.isFreePlan());

        playlistRepository.save(playlist);

        playlist.publishEvents(eventPublisher);

        return toResponse(playlist);
    }

    public PlaylistResponse removeMusic(String playlistId, String musicId, String userId) {
        PlaylistId id = new PlaylistId(playlistId);
        MusicId musicIdVO = new MusicId(musicId);
        UserId userIdVO = new UserId(userId);

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada"));

        if (!playlist.isOwnedBy(userIdVO)) {
            throw new ForbiddenException("Apenas o dono pode remover músicas");
        }

        Music music = musicRepository.findById(musicIdVO)
                .orElseThrow(() -> new ResourceNotFoundException("Música não encontrada"));

        playlist.removeMusic(music);

        playlistRepository.save(playlist);
        playlist.publishEvents(eventPublisher);

        return toResponse(playlist);
    }

    public PlaylistResponse reorderMusic(String playlistId, String musicId, int newPosition, String userId) {
        PlaylistId id = new PlaylistId(playlistId);
        MusicId musicIdVO = new MusicId(musicId);
        UserId userIdVO = new UserId(userId);

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada"));

        if (!playlist.isOwnedBy(userIdVO)) {
            throw new ForbiddenException("Apenas o dono pode reordenar músicas");
        }

        playlist.reorderMusic(musicIdVO, newPosition);

        playlistRepository.save(playlist);
        playlist.publishEvents(eventPublisher);

        return toResponse(playlist);
    }

    public PlaylistResponse changeName(String playlistId, String newName, String userId) {
        PlaylistId id = new PlaylistId(playlistId);
        PlaylistName name = new PlaylistName(newName);
        UserId userIdVO = new UserId(userId);

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada"));

        if (!playlist.isOwnedBy(userIdVO)) {
            throw new ForbiddenException("Apenas o dono pode alterar o nome");
        }

        if (playlistRepository.existsByNameAndOwner(name, userIdVO)) {
            throw new BusinessRuleException("Você já tem uma playlist com este nome");
        }

        playlist.changeName(name);

        playlistRepository.save(playlist);
        playlist.publishEvents(eventPublisher);

        return toResponse(playlist);
    }

    public PlaylistResponse changeVisibility(String playlistId, String visibility, String userId) {
        PlaylistId id = new PlaylistId(playlistId);
        PlaylistVisibility newVisibility = PlaylistVisibility.valueOf(visibility.toUpperCase());
        UserId userIdVO = new UserId(userId);

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada"));

        if (!playlist.isOwnedBy(userIdVO)) {
            throw new ForbiddenException("Apenas o dono pode alterar visibilidade");
        }

        playlist.changeVisibility(newVisibility);

        playlistRepository.save(playlist);
        playlist.publishEvents(eventPublisher);

        return toResponse(playlist);
    }

    public void deletePlaylist(String playlistId, String userId) {
        PlaylistId id = new PlaylistId(playlistId);
        UserId userIdVO = new UserId(userId);

        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist não encontrada"));

        if (!playlist.isOwnedBy(userIdVO)) {
            throw new ForbiddenException("Apenas o dono pode deletar a playlist");
        }

        playlistRepository.delete(playlist);
        playlist.publishEvents(eventPublisher);
    }

    private PlaylistResponse toResponse(Playlist playlist) {
        return new PlaylistResponse(
                playlist.id().value(),
                playlist.name().value(),
                playlist.ownerId().value(),
                playlist.musics().stream()
                        .map(m -> new MusicResponse(
                                m.id().value(),
                                m.title(),
                                m.artist().name(),
                                m.duration().toString(),
                                m.genre().name()
                        ))
                        .collect(Collectors.toList()),
                playlist.visibility().name(),
                playlist.musicCount(),
                playlist.totalDuration().toString(),
                playlist.createdAt(),
                playlist.updatedAt()
        );
    }
}


