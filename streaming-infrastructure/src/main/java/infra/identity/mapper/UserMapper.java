package infra.identity.mapper;

import identity.model.aggregate.User;
import identity.model.valueObject.*;
import infra.identity.entity.UserJpaEntity;
import infra.identity.entity.UserStatusEntity;
import org.springframework.stereotype.Component;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import shared.model.valueObject.UserId;

@Component("identityUserMapper")
public class UserMapper {

    public User toDomainUser(UserJpaEntity entity) {
        UserId userId = new UserId(entity.getId());
        FullName fullName = new FullName(entity.getFirstName(), entity.getLastName());
        Email email = new Email(entity.getEmail());
        Password password = new Password(entity.getPassword());
        DateOfBirth dateOfBirth = new DateOfBirth(entity.getDateOfBirth());

        return User.reconstruct(userId, fullName, email, password, dateOfBirth, UserStatus.valueOf(entity.getStatus().toString()), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.id().value());
        entity.setFirstName(user.name().firstName());
        entity.setLastName(user.name().lastName());
        entity.setEmail(user.email().value());
        entity.setPassword(user.password().value());
        entity.setDateOfBirth(user.dateOfBirth().value());
        entity.setStatus(UserStatusEntity.valueOf(user.status().toString()));
        entity.setCreatedAt(user.createdAt());
        entity.setUpdatedAt(user.updatedAt());
        return entity;
    }
}
