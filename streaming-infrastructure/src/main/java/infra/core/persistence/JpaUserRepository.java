package infra.core.persistence;


import core.model.aggregate.User;
import core.repository.UserRepository;
import infra.core.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import shared.model.valueObject.UserId;

import java.util.Optional;

@Component("coreJpaUserRepository")
public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper userMapper;

    public JpaUserRepository(UserJpaRepository jpaRepository, @Qualifier("coreUserMapper")UserMapper userMapper) {
        this.jpaRepository = jpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return jpaRepository.findById(userId.value())
                .map(userMapper::toDomainUser);
    }

    @Override
    public boolean existsById(UserId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void save(User user) {
        jpaRepository.save(userMapper.toJpaEntity(user));
    }

    @Override
    public void delete(User user) {
        jpaRepository.delete(userMapper.toJpaEntity(user));
    }

}
