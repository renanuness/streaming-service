package infra.core.entity;

import core.model.entity.Music;
import core.model.valueObject.PlaylistId;
import core.model.valueObject.PlaylistName;
import core.model.valueObject.PlaylistVisibility;
import infra.identity.entity.UserStatusEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import shared.model.valueObject.UserId;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "playlist")
@Data
@AllArgsConstructor
public class PlaylistJpaEntity{
    @Id
    private String id;

    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Column(name="ownerId", columnDefinition = "TEXT")
    private String ownerId;

    @Column(name="musics", columnDefinition = "TEXT")
    private String musics;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PlaylistVisibilityEntity status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    public PlaylistJpaEntity() {}
}
