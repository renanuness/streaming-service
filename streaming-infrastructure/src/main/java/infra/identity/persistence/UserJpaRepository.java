package infra.identity.persistence;

import infra.identity.entity.UserJpaEntity;
import infra.identity.entity.UserStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, String> {

    Optional<UserJpaEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    List<UserJpaEntity> findByStatus(UserStatusEntity status);
}
