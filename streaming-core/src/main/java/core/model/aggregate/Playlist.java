package core.model.aggregate;

import core.event.*;
import core.model.entity.Music;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import core.model.valueObject.PlaylistVisibility;
import shared.domain.AggregateRoot;
import shared.exception.BusinessRuleException;
import shared.model.valueObject.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist extends AggregateRoot {
    private static final int MAX_MUSICS = 1000;
    private static final int MAX_MUSICS_FREE_PLAN = 100;

    private final PlaylistId id;
    private PlaylistName name;
    private final UserId ownerId;
    private List<Music> musics;
    private PlaylistVisibility visibility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    Playlist(PlaylistName name, UserId ownerId) {
        super();
        this.id = PlaylistId.generate();
        this.name = name;
        this.ownerId = ownerId;
        this.musics = new ArrayList<>();
        this.visibility = PlaylistVisibility.PRIVATE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        registerEvent(new PlaylistCreatedEvent(this.id, this.name, this.ownerId));
    }

    private Playlist(
            PlaylistId id,
            PlaylistName name,
            UserId ownerId,
            List<Music> musics,
            PlaylistVisibility visibility,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super();
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.musics = new ArrayList<>(musics);
        this.visibility = visibility;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Playlist reconstruct(
            PlaylistId id,
            PlaylistName name,
            UserId ownerId,
            List<Music> musics,
            PlaylistVisibility visibility,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Playlist(id, name, ownerId, musics, visibility, createdAt, updatedAt);
    }

    public void addMusic(Music music, boolean isFreePlan) {
        if (this.visibility == PlaylistVisibility.DELETED) {
            throw new BusinessRuleException("Não é possível adicionar música a uma playlist excluída");
        }

        if (containsMusic(music)) {
            throw new BusinessRuleException("Esta música já está na playlist: " + music.title());
        }

        int maxLimit = isFreePlan ? MAX_MUSICS_FREE_PLAN : MAX_MUSICS;
        if (this.musics.size() >= maxLimit) {
            throw new BusinessRuleException(
                    String.format("Limite máximo de %d músicas atingido", maxLimit)
            );
        }

        this.musics.add(music);
        this.updatedAt = LocalDateTime.now();

        registerEvent(new MusicAddedToPlaylistEvent(this.id, this.ownerId, music.id()));
    }

    public void removeMusic(Music music) {
        if (!containsMusic(music)) {
            throw new BusinessRuleException("Música não encontrada na playlist: " + music.title());
        }

        this.musics.remove(music);
        this.updatedAt = LocalDateTime.now();

        registerEvent(new MusicRemovedFromPlaylistEvent(this.id, this.ownerId, music.id()));
    }

    public void changeName(PlaylistName newName) {
        if (this.name.equals(newName)) {
            throw new BusinessRuleException("Novo nome é igual ao atual");
        }

        PlaylistName oldName = this.name;
        this.name = newName;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new PlaylistNameChangedEvent(this.id, oldName, newName));
    }

    public void changeVisibility(PlaylistVisibility newVisibility) {
        if (this.visibility == newVisibility) {
            throw new BusinessRuleException("Visibilidade já é " + newVisibility.description());
        }

        PlaylistVisibility oldVisibility = this.visibility;
        this.visibility = newVisibility;
        this.updatedAt = LocalDateTime.now();

        registerEvent(new PlaylistVisibilityChangedEvent(this.id, oldVisibility, newVisibility));
    }

    public boolean containsMusic(Music music) {
        return musics.stream().anyMatch(m -> m.equals(music));
    }

    public int musicCount() {
        return musics.size();
    }

    public boolean isEmpty() {
        return musics.isEmpty();
    }

    public boolean isOwnedBy(UserId userId) {
        return this.ownerId.equals(userId);
    }


    public boolean isVisibleTo(UserId userId) {
        return this.visibility == PlaylistVisibility.PUBLIC ||
                this.ownerId.equals(userId) ||
                this.visibility == PlaylistVisibility.SHARED;
    }

    public PlaylistId id() { return id; }
    public PlaylistName name() { return name; }
    public UserId ownerId() { return ownerId; }
    public List<Music> musics() { return Collections.unmodifiableList(musics); }
    public PlaylistVisibility visibility() { return visibility; }
}