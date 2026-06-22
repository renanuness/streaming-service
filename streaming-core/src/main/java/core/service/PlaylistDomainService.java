package core.service;

import core.model.aggregate.Playlist;
import core.model.entity.Music;
import core.model.valueObject.MusicDuration;
import core.model.valueObject.PlaylistName;
import shared.exception.BusinessRuleException;
import shared.model.valueObject.UserId;

import java.util.List;

public class PlaylistDomainService {
    private static final int MAX_PLAYLISTS_FREE = 50;
    private static final int MAX_PLAYLISTS_PREMIUM = 1000;
    private static final int MAX_MUSICS_FREE = 100;
    private static final int MAX_MUSICS_PREMIUM = 1000;

    public Playlist createPlaylist(PlaylistName name, UserId ownerId) {
        return new Playlist(name, ownerId);
    }

    public boolean canCreatePlaylist(int currentCount, boolean isPremium) {
        int maxPlaylists = isPremium ? MAX_PLAYLISTS_PREMIUM : MAX_PLAYLISTS_FREE;
        return currentCount < maxPlaylists;
    }

    public boolean canAddMusic(Playlist playlist, boolean isFreePlan) {
        int maxMusics = isFreePlan ? MAX_MUSICS_FREE : MAX_MUSICS_PREMIUM;
        return playlist.musicCount() < maxMusics;
    }


    public void validateMusicAddition(Playlist playlist, Music music, boolean isFreePlan) {
        if (playlist.isDeleted()) {
            throw new BusinessRuleException("Não é possível adicionar música a uma playlist excluída");
        }

        if (playlist.containsMusic(music)) {
            throw new BusinessRuleException("Esta música já está na playlist");
        }

        int maxMusics = isFreePlan ? MAX_MUSICS_FREE : MAX_MUSICS_PREMIUM;
        if (playlist.musicCount() >= maxMusics) {
            throw new BusinessRuleException(
                    String.format("Limite de %d músicas atingido para o plano %s. " +
                                    "Faça upgrade para adicionar mais músicas!",
                            maxMusics, isFreePlan ? "gratuito" : "premium")
            );
        }
    }

    public MusicDuration calculateTotalDuration(List<Music> musics) {
        int totalSeconds = musics.stream()
                .mapToInt(m -> m.duration().toSeconds())
                .sum();


        return new MusicDuration(totalSeconds);
    }
}
