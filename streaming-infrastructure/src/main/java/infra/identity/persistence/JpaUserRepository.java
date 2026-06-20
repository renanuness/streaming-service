package infra.identity.persistence;

import identity.model.aggregate.User;
import shared.model.valueObject.Email;
import shared.model.valueObject.UserId;
import identity.model.valueObject.UserStatus;
import identity.repository.UserRepository;
import infra.identity.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    public JpaUserRepository(UserJpaRepository jpaRepository, UserMapper userMapper) {
        this.jpaRepository = jpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaRepository.findById(userId.value())
                .map(userMapper::toDomainUser);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value())
                .map(userMapper::toDomainUser);
    }

    @Override
    public void save(User user) {
        jpaRepository.save(userMapper.toJpaEntity(user));
    }

    @Override
    public boolean existsByEmail(Email email) {
        return false;
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public List<User> findByStatus(UserStatus status) {
        return List.of();
    }
}
