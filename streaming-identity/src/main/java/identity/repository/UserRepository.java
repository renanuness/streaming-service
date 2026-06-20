package identity.repository;

import identity.model.aggregate.User;
import shared.model.valueObject.Email;
import shared.model.valueObject.UserId;
import identity.model.valueObject.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    void save(User user);
    Optional<User> findById(UserId userId);
    Optional<User> findByEmail(Email email);
    boolean existsByEmail(Email email);
    void delete(User user);
    List<User> findAll();
    List<User> findByStatus(UserStatus status);
}
