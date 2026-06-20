package infra.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "playlist")
@Data
@AllArgsConstructor
public class PlaylistJpaEntity{
    @Id
    private Long id;

    public PlaylistJpaEntity() {
    }
}
