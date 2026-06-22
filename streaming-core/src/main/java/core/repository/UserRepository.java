package core.repository;

import core.model.aggregate.User;
import shared.model.valueObject.UserId;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    void delete(User user);
    Optional<User> findById(UserId id);
    boolean existsById(UserId id);
}
