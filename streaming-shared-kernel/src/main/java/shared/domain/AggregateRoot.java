package shared.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<DomainEvent> events;

    protected AggregateRoot() {

        this.events = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected void registerEvent(DomainEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Evento não pode ser nulo");
        }
        this.events.add(event);
        this.updatedAt = LocalDateTime.now();
    }

    public List<DomainEvent> domainEvents() {
        return Collections.unmodifiableList(events);
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public List<DomainEvent> collectEvents() {
        List<DomainEvent> pendingEvents = Collections.unmodifiableList(new ArrayList<>(events));
        this.events.clear();
        return pendingEvents;
    }

    public void publishEvents(DomainEventPublisher publisher) {
        if (!events.isEmpty()) {
            events.forEach(publisher::publish);
            events.clear();
        }
    }

    public int eventCount() {
        return events.size();
    }
}