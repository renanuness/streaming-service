package identity.model.aggregate;

import shared.domain.AggregateRoot;
import identity.event.UserAccountDeletedEvent;
import shared.model.valueObject.FullName;
import shared.exception.BusinessRuleException;
import shared.model.valueObject.Email;
import identity.model.valueObject.Password;
import shared.model.valueObject.UserId;
import identity.model.valueObject.UserStatus;
import identity.model.valueObject.DateOfBirth;

import java.time.LocalDateTime;
import java.util.UUID;

public class User extends AggregateRoot {

    private final UserId id;
    private FullName name;
    private Email email;
    private Password password;
    private DateOfBirth dateOfBirth;
    private UserStatus status;

    public User(FullName name, Email email, Password password, DateOfBirth dateOfBirth) {
        super();
        this.id = new UserId(UUID.randomUUID().toString());
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        status = UserStatus.ACTIVE;
    }

    private User(
            UserId id,
            FullName name,
            Email email,
            Password password,
            DateOfBirth dateOfBirth,
            UserStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
    }

    public static User reconstruct(
            UserId id,
            FullName name,
            Email email,
            Password password,
            DateOfBirth dateOfBirth,
            UserStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new User(id, name, email, password, dateOfBirth, status, createdAt, updatedAt);
    }

    public UserId id() { return id; }
    public FullName name() { return name; }
    public Email email() { return email; }
    public DateOfBirth dateOfBirth() { return dateOfBirth; }
    public Password password(){ return password; }
    public UserStatus status(){ return status; }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public void updateName(FullName newName) {
        this.name = newName;
    }

    public void updatePassword(Password currentPassword, Password newPassword) {
        if (!this.password.equals(currentPassword)) {
            throw new BusinessRuleException("Senha atual incorreta");
        }
        this.password = newPassword;
    }

    public boolean verifyCredentials(String rawPassword) {
        return password().matches(rawPassword);
    }

    public void deleteAccount(Password currentPassword) {
        if (!this.password.matches(currentPassword.value())) {
            throw new BusinessRuleException("Senha incorreta para exclusão da conta");
        }

        if (this.status == UserStatus.DELETED) {
            throw new BusinessRuleException("Conta já foi excluída");
        }

        this.status = UserStatus.DELETED;

        registerEvent(new UserAccountDeletedEvent(this.id, email));
    }
}