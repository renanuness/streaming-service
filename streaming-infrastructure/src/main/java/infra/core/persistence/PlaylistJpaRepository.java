package infra.core.persistence;

import infra.core.entity.PlaylistJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistJpaRepository extends JpaRepository<PlaylistJpaEntity, String> {
}
