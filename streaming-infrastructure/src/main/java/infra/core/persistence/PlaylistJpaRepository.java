package infra.core.persistence;

import infra.core.entity.PlaylistJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistJpaRepository extends JpaRepository<PlaylistJpaEntity, String> {
    @Query("SELECT COUNT(p) > 0 FROM PlaylistJpaEntity p " +
            "WHERE p.name = :name AND p.ownerId = :ownerId")
    boolean existsByNameAndOwnerId(@Param("name") String name,
                                   @Param("ownerId") String ownerId);

    @Query("SELECT COUNT(p) > 0 FROM PlaylistJpaEntity p " +
            "WHERE p.ownerId = :ownerId")
    int countByOwner(@Param("ownerId")String ownerId);
}
