package identity.event;

import shared.domain.DomainEvent;
import identity.model.valueObject.DateOfBirth;
import shared.model.valueObject.Email;
import shared.model.valueObject.FullName;
import shared.model.valueObject.UserId;

import java.time.LocalDate;

public class UserRegisteredEvent extends DomainEvent {

    private final String userId;
    private final String email;
    private final String fullName;
    private final LocalDate dateOfBirth;

    public UserRegisteredEvent(UserId userId, Email email, FullName fullName, DateOfBirth dateOfBirth) {
        super();
        this.userId = userId.value();
        this.email = email.value();
        this.fullName = fullName.fullName();
        this.dateOfBirth = dateOfBirth.value();
    }

    public String userId() { return userId; }
    public String email() { return email; }
    public String fullName() { return fullName; }
    public LocalDate dateOfBirth() { return dateOfBirth; }

    @Override
    public String eventType() {
        return "user.registered";
    }
}
