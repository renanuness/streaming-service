package shared.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {

    private final String eventId;
    private final LocalDateTime occurredAt;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = LocalDateTime.now();
    }

    public String eventId() {
        return eventId;
    }

    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    public abstract String eventType();
}