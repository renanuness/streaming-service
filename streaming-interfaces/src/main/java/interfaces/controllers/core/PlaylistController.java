package interfaces.controllers.core;

import application.core.dto.AddMusicToPlaylistRequest;
import application.core.dto.PlaylistResponse;
import application.core.service.PlaylistApplicationService;
import interfaces.dto.core.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistApplicationService playlistService;

    public PlaylistController(PlaylistApplicationService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<PlaylistResponse> createPlaylist(
            @Valid @RequestBody CreatePlaylistRequest request
    ) {
        PlaylistResponse response = playlistService.createPlaylist(
                    request.name(),
                    request.ownerId(),
                    request.isPremium()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> getPlaylist(
            @PathVariable String playlistId
    ) {
        PlaylistResponse response;
        response = playlistService.findById(playlistId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistResponse>> getUserPlaylists(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<PlaylistResponse> playlists = playlistService.findByOwner(userId);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<PlaylistCountResponse> countPlaylists(
            @PathVariable String userId
    ) {
        int count = playlistService.countByOwner(userId);
        return ResponseEntity.ok(new PlaylistCountResponse(userId, count));
    }

    @PostMapping("/{playlistId}/musics")
    public ResponseEntity<PlaylistResponse> addMusic(
            @PathVariable String playlistId,
            @Valid @RequestBody AddMusicRequest request
    ) {
        PlaylistResponse response = playlistService.addMusic(
                new AddMusicToPlaylistRequest(
                        playlistId,
                        request.musicId(),
                        request.userId(),
                        request.isFreePlan()
                )
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{playlistId}/musics/{musicId}")
    public ResponseEntity<PlaylistResponse> removeMusic(
            @PathVariable String playlistId,
            @PathVariable String musicId,
            @RequestParam @NotBlank String userId
    ) {
        PlaylistResponse response = playlistService.removeMusic(playlistId, musicId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{playlistId}/musics/{musicId}/position")
    public ResponseEntity<PlaylistResponse> reorderMusic(
            @PathVariable String playlistId,
            @PathVariable String musicId,
            @Valid @RequestBody ReorderMusicRequest request
    ) {
        PlaylistResponse response = playlistService.reorderMusic(
                playlistId,
                musicId,
                request.newPosition(),
                request.userId()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{playlistId}/name")
    public ResponseEntity<PlaylistResponse> changeName(
            @PathVariable String playlistId,
            @Valid @RequestBody ChangeNameRequest request
    ) {
        PlaylistResponse response = playlistService.changeName(
                playlistId,
                request.newName(),
                request.userId()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{playlistId}/visibility")
    public ResponseEntity<PlaylistResponse> changeVisibility(
            @PathVariable String playlistId,
            @Valid @RequestBody ChangeVisibilityRequest request
    ) {
        PlaylistResponse response = playlistService.changeVisibility(
                playlistId,
                request.visibility(),
                request.userId()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(
            @PathVariable String playlistId,
            @RequestParam @NotBlank String userId
    ) {
        playlistService.deletePlaylist(playlistId, userId);
        return ResponseEntity.noContent().build();
    }
}