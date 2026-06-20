package identity.event;

import shared.domain.DomainEvent;
import shared.model.valueObject.Email;
import shared.model.valueObject.UserId;

import java.time.LocalDateTime;

public class UserAccountDeletedEvent extends DomainEvent {

    private final String userId;
    private final String userEmail;
    private final LocalDateTime deletionDate;

    public UserAccountDeletedEvent(UserId userId, Email userEmail) {
        super();
        this.userId = userId.value();
        this.userEmail = userEmail.value();
        this.deletionDate = LocalDateTime.now();
    }

    public String userId() {
        return userId;
    }

    public String userEmail() {
        return userEmail;
    }

    public LocalDateTime deletionDate() {
        return deletionDate;
    }

    @Override
    public String eventType() {
        return "user.account.deleted";
    }
}